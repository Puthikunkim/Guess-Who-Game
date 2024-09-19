package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/**
 * Controller class for the room view. Handles user interactions within the room where the user can
 * chat with customers and guess their profession.
 */
public class RoomController extends Controller {

  public static GameStateContext context = new GameStateContext();

  @FXML private Button btnGuess;
  @FXML private Label lblProfession;
  @FXML private Label timerLabel; //
  @FXML private Rectangle rectCashier;
  @FXML private Rectangle rectChest; //
  @FXML private Rectangle rectPerson1;
  @FXML private Rectangle rectPerson2;
  @FXML private Rectangle rectPerson3;
  @FXML private TextArea txtaChat; //

  // chat-room
  @FXML private Button btnCrimeScene;
  @FXML private Button btnJimmy;
  @FXML private Button btnGrandma;
  @FXML private Button btnBusinessman;

  /**
   * Initializes the room view. If it's the first time initialization, it will provide instructions.
   */
  @FXML
  public void initialize() {
    // Clue and win conditions chat initialisation
    txtaChat.appendText("To Win You Must:");
    txtaChat.appendText("\n\n");
    txtaChat.appendText("- Chat with all 3 suspects.");
    txtaChat.appendText("\n");
    txtaChat.appendText("- Find 1 of the 3 clues.");
    txtaChat.appendText("\n");
    txtaChat.appendText("- Guess who the thief of the vase is.");
    txtaChat.appendText("\n");
    txtaChat.appendText("-------------------------------");
    txtaChat.appendText("\n");
    txtaChat.appendText("Clues Gathered:");
    txtaChat.appendText("\n\n");
  }

  /** when switched to for the first time set state to gameStarted */
  @Override
  public void onSwitchTo() {
    btnCrimeScene.setDisable(true);
    btnGuess.setDisable(true);
    if (BusinessmanController.businessmanChatted == true
        && GrandmaController.grandmaChatted == true
        && JimmyController.jimmyChatted == true) {
      btnGuess.setDisable(false);
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
   * Handles the switch button click event to grandma's scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleGrandmaClick(ActionEvent event) throws IOException {
    GrandmaController grandmaController =
        (GrandmaController) SceneManager.getController(AppUi.GRANDMA_ROOM);
    SceneManager.switchRoot(AppUi.GRANDMA_ROOM);
    grandmaController.startChat();
  }

  /**
   * Handles the switch button click event to grandma's scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleBusinessmanClick(ActionEvent event) throws IOException {
    BusinessmanController businessmanController =
        (BusinessmanController) SceneManager.getController(AppUi.BUSINESSMAN_ROOM);
    SceneManager.switchRoot(AppUi.BUSINESSMAN_ROOM);
    businessmanController.startChat();
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException {
    GuessingController guessingController =
        (GuessingController) SceneManager.getController(AppUi.GUESSING_ROOM);
    SceneManager.switchRoot(AppUi.GUESSING_ROOM);
    guessingController.startChat();
    context.handleGuessClick();
  }

  public static GameStateContext getContext() {
    return context;
  }
}
