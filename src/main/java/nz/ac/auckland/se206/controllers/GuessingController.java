package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.prompts.PromptEngineering;

/**
 * Controller class for the room view. Handles user interactions within the room where the user can
 * chat with customers and guess their profession.
 */
public class GuessingController extends Controller {

  private ChatCompletionRequest
      chatCompletionRequestGuessing; // Chat completion requests for each suspect
  private boolean guessingStarted = false;

  @FXML private Button btnJimmy;
  @FXML private Button btnGrandma;
  @FXML private Button btnBusinessman;

  @FXML private Label lblResponse;
  @FXML private Label timerLabel;
  @FXML private Label lblGuess;

  @FXML private TextArea txtaChat1;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;
  @FXML private Button btnEndGame;

  /**
   * Initializes the room view. If it's the first time initialization, it will provide instructions
   * via text-to-speech.
   */
  @FXML
  public void initialize() {
    lblResponse.setVisible(false);
    btnEndGame.setDisable(true);
    btnSend.setDisable(true);
    txtaChat1.appendText("To Win You Must:");
    txtaChat1.appendText("\n\n");
    txtaChat1.appendText("Select the correct thief.");
    txtaChat1.appendText("\n");
    txtaChat1.appendText("If you guess incorrectly, you will be sent to the game over screen.");
    txtaChat1.appendText("\n\n");
    btnEndGame.setDisable(true);
    // Add event handler for pressing Enter in txtInput
    txtInput.setOnKeyPressed(
        event -> {
          switch (event.getCode()) {
            case ENTER:
              try {
                onSendMessage(new ActionEvent()); // Trigger the send message method
              } catch (ApiProxyException | IOException e) {
                e.printStackTrace();
              }
              break;
            default:
              break;
          }
        });
  }

  /**
   * Updates the timer label with the given time string.
   *
   * @param timeString the time string to display
   */
  public void updateTimer(String timeString) {
    timerLabel.setText(timeString + "\n" + "Remaining");
  }

  public void makeGuessBusinessman() {
    txtaChat1.appendText("Game" + ": " + "Please enter the reason you chose this suspect" + "\n\n");
    btnJimmy.setDisable(true);
    btnGrandma.setDisable(true);
    btnBusinessman.setDisable(true);
    btnSend.setDisable(false);
    lblGuess.setText("Guess is correct");
  }

  public void makeGuessGrandma() {
    SceneManager.switchRoot(SceneManager.AppUi.GAMEOVER_ROOM);
  }

  public void makeGuessJimmy() {
    SceneManager.switchRoot(SceneManager.AppUi.GAMEOVER_ROOM);
  }

  /**
   * Generates the system prompt based on who the suspect is.
   *
   * @return the system prompt string
   */
  private String getSystemPrompt() {
    Map<String, String> map = new HashMap<>();
    map.put("profession", "not thief");
    return PromptEngineering.getPrompt("guessing.txt", map);
  }

  /**
   * Sets the suspect for the chat context and initializes the ChatCompletionRequest.
   *
   * @param profession the profession to set
   */
  public void startChat() {
    if (!guessingStarted) {
      try {
        ApiProxyConfig config = ApiProxyConfig.readConfig();
        chatCompletionRequestGuessing =
            new ChatCompletionRequest(config)
                .setN(1)
                .setTemperature(0.2)
                .setTopP(0.5)
                .setMaxTokens(100);
        runGpt(new ChatMessage("system", getSystemPrompt()));
        guessingStarted = true;
      } catch (ApiProxyException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {

    // Append the user chat message to the relevant chat area for the suspect.
    if (msg.getRole().equals("user")) {
      txtaChat1.appendText("You" + ": " + msg.getContent() + "\n\n");
    }

    // Append the assistant chat message to the relevant chat area for the suspect with the name of
    // the suspect.
    if (msg.getRole().equals("assistant")) {
      txtaChat1.appendText("Game" + ": " + msg.getContent() + "\n\n");
    }
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    lblResponse.setVisible(true);
    lblResponse.setText("Please wait...");
    btnJimmy.setDisable(true);
    btnGrandma.setDisable(true);
    btnBusinessman.setDisable(true);

    // Create a Task for the background thread to run the GPT model
    Task<Void> backgroundTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            chatCompletionRequestGuessing.addMessage(msg);
            try {
              ChatCompletionResult chatCompletionResult = chatCompletionRequestGuessing.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              chatCompletionRequestGuessing.addMessage(result.getChatMessage());

              // Update the UI on the JavaFX Application Thread
              Platform.runLater(
                  () -> {
                    appendChatMessage(result.getChatMessage());
                    lblResponse.setVisible(false);
                    btnJimmy.setDisable(false);
                    btnGrandma.setDisable(false);
                    btnBusinessman.setDisable(false);
                  });
            } catch (ApiProxyException e) {
              e.printStackTrace();
            }
            return null;
          }
        };

    // Create and start the background thread
    Thread backgroundThread = new Thread(backgroundTask);
    backgroundThread.setDaemon(true); // Ensure the thread does not prevent JVM shutdown
    backgroundThread.start();

    return null;
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    String message = txtInput.getText().trim();
    if (message.isEmpty() || btnSend.isDisabled()) {
      return;
    }
    txtInput.clear();
    // Create a ChatMessage object with the user's message, append it to the chat area and get a
    // response from the GPT model
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);
    runGpt(msg);
    btnEndGame.setDisable(false);
    btnSend.setDisable(true);
    RoomController.context.stopTimer();
  }

  /**
   * Handles the switch button click event to game over scene.
   *
   * @param event the action event triggered by clicking the game over button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onEndGame(ActionEvent event) throws ApiProxyException, IOException {
    SceneManager.switchRoot(SceneManager.AppUi.GAMEOVER_ROOM);
  }
}
