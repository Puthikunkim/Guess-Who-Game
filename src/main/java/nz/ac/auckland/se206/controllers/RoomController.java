package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.apiproxy.tts.TextToSpeechRequest.Voice;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.Person;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.prompts.PromptEngineering;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * Controller class for the room view. Handles user interactions within the room where the user can
 * chat with customers and guess their profession.
 */
public class RoomController extends Controller {

  private static GameStateContext context = new GameStateContext();

  @FXML private Button btnGuess;
  @FXML private Button btnSend; //
  @FXML private Label lblProfession;
  @FXML private Label timerLabel; //
  @FXML private Rectangle rectCashier;
  @FXML private Rectangle rectChest; //
  @FXML private Rectangle rectPerson1;
  @FXML private Rectangle rectPerson2;
  @FXML private Rectangle rectPerson3;
  @FXML private TextArea txtaChat; //
  @FXML private TextField txtInput; //

  // chat-room
  @FXML private Button jimmy;

  private boolean timerStarted = false; //
  private ChatCompletionRequest chatCompletionRequest; //
  private int investigatingTime = 120; //
  private Person currentPerson; //
  private Timer timer; //
  private Voice narratorVoice = Voice.GOOGLE_EN_AU_STANDARD_D; //

  /** when switched to for the first time set state to gameStarted */
  @Override
  public void onSwitchTo() {
    if (!timerStarted) {
      context.setState(context.getGameStartedState());
    }
  }

  /** Starts 120 Timer for investigation, goes from 0 to -10 for guessing time */
  public void startTimer() {
    timer = new Timer();
    timerStarted = true;

    TimerTask gameEnd =
        new TimerTask() {

          public void run() {

            Platform.runLater(
                () -> {
                  investigatingTime -= 1;
                  if (investigatingTime > 0) {
                    timerLabel.setText("SECONDS REMAINING: " + investigatingTime);
                  } else if (investigatingTime > -10) {
                    timerLabel.setText("GUESSING TIME LEFT : " + (10 + investigatingTime));
                    if (context.getState() != context.getGuessingState()) {
                      context.setState(context.getGuessingState());
                    }
                  } else {

                    context.setState(context.getGameOverState());
                    timer.cancel();
                  }
                });
          }
        };

    timer.scheduleAtFixedRate(gameEnd, 1000, 1000);
  }

  public void guessingMode() {
    btnSend.setDisable(true);
    txtInput.setDisable(true);
    btnGuess.setDisable(true);
  }

  /**
   * Handles mouse clicks on rectangles representing people in the room.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRectangle.getId());
  }

  // chat-room

  /**
   * Handles the switch button click event to jimmy's scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleJimmyClick(ActionEvent event) throws IOException {
    JimmyController jimmyController =
        (JimmyController) SceneManager.getController(AppUi.JIMMY_ROOM);
    SceneManager.switchRoot(AppUi.JIMMY_ROOM);
    jimmyController.startChat();
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException {
    context.handleGuessClick();
  }

  /**
   * Generates the system prompt based on the profession.
   *
   * @return the system prompt string
   */
  private String getSystemPrompt() {
    Map<String, String> map = new HashMap<>();

    // Add extra system dialog if we have found evidence of thief in chest
    if (currentPerson.getIsThief()) {
      map.put("profession", "thief");
      return PromptEngineering.getPrompt(currentPerson.getName() + ".txt", map);
    }
    map.put("profession", "not thief");
    return PromptEngineering.getPrompt(currentPerson.getName() + ".txt", map);
  }

  /**
   * Sets the profession for the chat context and initializes the ChatCompletionRequest.
   *
   * @param profession the profession to set
   */
  public void setPerson(Person person) {
    // Set name and proffession of current character you are talking to
    this.currentPerson = person;

    // Create a ApiProxy and Chat request for ChatGPT
    try {
      // Api proxy setup
      ApiProxyConfig config = ApiProxyConfig.readConfig();

      // Chat Completion Request for GPT
      chatCompletionRequest =
          new ChatCompletionRequest(config)
              .setN(1)
              .setTemperature(0.2)
              .setTopP(0.5)
              .setMaxTokens(100);

      context.playSound("Approach" + currentPerson.getName());
      // Create chat message that only gets appended to the chat log not to ChatGPT
      ChatMessage introMessage =
          new ChatMessage(
              "Narrator", "You are approaching " + currentPerson.getName() + " what do you say");
      appendChatMessage(introMessage);
      // set GPTs system prompt
      runGpt(new ChatMessage("system", getSystemPrompt()));

    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  public void appendChatMessage(ChatMessage msg) {
    txtaChat.appendText(msg.getRole() + " : " + msg.getContent() + "\n\n");

    if (msg.getRole().equals("Narrator") || currentPerson == null) {
      return; // Stopping narrator TTS but leaving it here for future
    }
    Voice voice = currentPerson.getVoice();
    TextToSpeech.speak(msg.getContent(), voice);
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private void runGpt(ChatMessage msg) throws ApiProxyException {
    // Create task for GPT Async Call
    Task<ChatCompletionResult> runGptAsyncTask =
        new Task<ChatCompletionResult>() {
          protected ChatCompletionResult call() throws Exception {
            chatCompletionRequest.addMessage(msg);
            try {
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              return chatCompletionResult;
            } catch (ApiProxyException e) {
              e.printStackTrace();
              return null;
            }
          }
        };
    // Create and start GPT async thread
    Thread gptThread = new Thread(runGptAsyncTask);
    gptThread.setDaemon(timerStarted);
    gptThread.start();
    // When Starting gpt call disable most button
    enableButtons(false);
    // When GPT call succeds enable buttons and display response
    runGptAsyncTask.setOnSucceeded(
        event -> {
          // get response
          Choice result = runGptAsyncTask.getValue().getChoices().iterator().next();
          chatCompletionRequest.addMessage(result.getChatMessage());
          // Display Response
          ChatMessage displayedText =
              new ChatMessage(currentPerson.getName(), result.getChatMessage().getContent());
          appendChatMessage(displayedText);
          // Enable buttons
          enableButtons(true);
        });
  }

  private void enableButtons(boolean enable) {

    btnSend.setDisable(!enable);
    rectChest.setDisable(!enable);
    rectPerson1.setDisable(!enable);
    rectPerson2.setDisable(!enable);
    rectPerson3.setDisable(!enable);
    btnGuess.setDisable(!enable);
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
    if (message.isEmpty()) {
      return;
    }
    txtInput.clear();
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);
    runGpt(msg);
  }

  public void setInvestigatingTime(int investigatingTime) {
    this.investigatingTime = investigatingTime;
  }

  public Label getTimerLabel() {
    return timerLabel;
  }

  public Timer getTimer() {
    return timer;
  }

  public GameStateContext getContext() {
    return context;
  }
}
