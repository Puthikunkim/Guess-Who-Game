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
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.prompts.PromptEngineering;

/**
 * Controller class for the room view. Handles user interactions within the room where the user can
 * chat with customers and guess their profession.
 */
public class JimmyController extends Controller {

  public static boolean jimmyChatted = false;
  private ChatCompletionRequest
      chatCompletionRequestChild; // Chat completion requests for each suspect
  private boolean jimmyStarted = false;

  @FXML private Button btnCrimeScene;
  @FXML private Button btnJimmy;
  @FXML private Button btnGrandma;
  @FXML private Button btnBusinessman;
  @FXML private Button btnMakeGuess;

  @FXML private Label lblResponse;
  @FXML private Label timerLabel; //

  @FXML private TextArea txtaChat1;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;

  private boolean canSwitch = true; // Boolean to track if the user can switch suspects

  /**
   * Initializes the room view. If it's the first time initialization, it will provide instructions
   * via text-to-speech.
   */
  @FXML
  public void initialize() {
    lblResponse.setVisible(false);
    btnMakeGuess.setDisable(true);
    startChat(); // Start the chat with the suspect to avoid lag when clicking on suspect for first
    // time
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

  /** when switched to disable button */
  @Override
  public void onSwitchTo() {
    btnJimmy.setDisable(true);
    btnMakeGuess.setDisable(true);
    if (SceneManager.getIfCanGuess()) {
      btnMakeGuess.setDisable(false);
    }
  }

  /**
   * Updates the timer label with the given time string.
   *
   * @param timeString the time string to display
   */
  public void updateTimer(String timeString) {
    timerLabel.setText(timeString + "\n" + "Remaining");
  }

  /**
   * Handles the switch button click event to jimmy's scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onCrimeSceneClick(ActionEvent event) throws IOException {
    SceneManager.switchRoot(AppUi.MAIN_ROOM);
    txtaChat1.clear();
  }

  /**
   * Handles the switch button click event to grandma's scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGrandmaClick(ActionEvent event) throws IOException {
    GrandmaController grandmaController =
        (GrandmaController) SceneManager.getController(AppUi.GRANDMA_ROOM);
    SceneManager.switchRoot(AppUi.GRANDMA_ROOM);
    grandmaController.startChat();
    txtaChat1.clear();
  }

  /**
   * Handles the switch button click event to grandma's scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onBusinessmanClick(ActionEvent event) throws IOException {
    BusinessmanController businessmanController =
        (BusinessmanController) SceneManager.getController(AppUi.BUSINESSMAN_ROOM);
    SceneManager.switchRoot(AppUi.BUSINESSMAN_ROOM);
    businessmanController.startChat();
    txtaChat1.clear();
  }

  /**
   * Generates the system prompt based on who the suspect is.
   *
   * @return the system prompt string
   */
  private String getSystemPrompt() {
    Map<String, String> map = new HashMap<>();
    map.put("profession", "not thief");
    return PromptEngineering.getPrompt("jimmy.txt", map);
  }

  /**
   * Sets the suspect for the chat context and initializes the ChatCompletionRequest.
   *
   * @param profession the profession to set
   */
  public void startChat() {
    // If the chat with the suspect has not started, initialize the ChatCompletionRequest and start
    if (!jimmyStarted) {
      try {
        ApiProxyConfig config = ApiProxyConfig.readConfig();
        chatCompletionRequestChild =
            new ChatCompletionRequest(config)
                .setN(1)
                .setTemperature(0.2)
                .setTopP(0.5)
                .setMaxTokens(100);
        runGpt(new ChatMessage("system", getSystemPrompt()));
        jimmyStarted = true;
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
      txtaChat1.appendText("Jimmy" + ": " + msg.getContent() + "\n\n");
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
    lblResponse.setText("Jimmy is responding...");
    canSwitch = false;
    enableButtons(canSwitch, SceneManager.getIfCanGuess());

    // Create a Task for the background thread to run the GPT model
    Task<Void> backgroundTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            chatCompletionRequestChild.addMessage(msg);
            try {
              ChatCompletionResult chatCompletionResult = chatCompletionRequestChild.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              chatCompletionRequestChild.addMessage(result.getChatMessage());

              // Update the UI on the JavaFX Application Thread
              Platform.runLater(
                  () -> {
                    appendChatMessage(result.getChatMessage());
                    lblResponse.setVisible(false);
                    canSwitch = true;
                    enableButtons(canSwitch, SceneManager.getIfCanGuess());
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

  private void enableButtons(boolean enable, boolean canGuess) {
    btnCrimeScene.setDisable(!enable);
    btnGrandma.setDisable(!enable);
    btnBusinessman.setDisable(!enable);
    btnSend.setDisable(!enable);
    if (canGuess == true) {
      btnMakeGuess.setDisable(!enable);
    }
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
    if (message.isEmpty() || !canSwitch) {
      return;
    }
    txtInput.clear();
    jimmyChatted = true;
    if (SceneManager.getIfCanGuess()) {
      btnMakeGuess.setDisable(false);
    }
    // Create a ChatMessage object with the user's message, append it to the chat area and get a
    // response from the GPT model
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);
    runGpt(msg);
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGuessClick(ActionEvent event) throws IOException {
    GuessingController guessingController =
        (GuessingController) SceneManager.getController(AppUi.GUESSING_ROOM);
    SceneManager.switchRoot(AppUi.GUESSING_ROOM);
    guessingController.startChat();
    RoomController.context.handleGuessClick();
  }
}
