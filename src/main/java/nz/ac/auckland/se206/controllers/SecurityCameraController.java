package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class SecurityCameraController extends Controller {
  private static GameStateContext context = new GameStateContext();
  @FXML private ImageView cameraDisplayImage;
  @FXML private ImageView staticImageView;
  private Image beforeImage = new Image("images/Vase.png");

  private boolean foundTimeOfTheft = false;

  // chat-room
  @FXML private Label timerLabel; //
  @FXML private Button btnCrimeScene;
  @FXML private Button btnJimmy;
  @FXML private Button btnGrandma;
  @FXML private Button btnBusinessman;
  @FXML private Button btnGuess;
  @FXML private TextArea txtaChat;

  @FXML
  private void onBeforeTimeClick() {
    cameraDisplayImage.setImage(beforeImage);
    staticImageView.setOpacity(0);
  }

  @FXML
  private void onAfterTimeClick() {

    cameraDisplayImage.setImage(null);
    staticImageView.setOpacity(0);
  }

  /** Correct Time is 2:00 */
  @FXML
  private void onDuringTimeClick() {
    staticImageView.setOpacity(100);
    foundTimeOfTheft = true;
  }

  @FXML
  private void onBackPressed() {
    SceneManager.switchRoot(AppUi.MAIN_ROOM);
  }

  /** when switched to disable button */
  @Override
  public void onSwitchTo() {
    btnGuess.setDisable(true);
    if (BusinessmanController.businessmanChatted == true
        && GrandmaController.grandmaChatted == true
        && JimmyController.jimmyChatted == true) {
      btnGuess.setDisable(false);
    }
  }

  /**
   * Handles the switch button click event to jimmy's scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleCrimeSceneClick(ActionEvent event) throws IOException {
    SceneManager.switchRoot(AppUi.MAIN_ROOM);
    txtaChat.clear();
  }

  @FXML
  private void handleJimmyClick(ActionEvent event) throws IOException {
    JimmyController jimmyController =
        (JimmyController) SceneManager.getController(AppUi.JIMMY_ROOM);
    SceneManager.switchRoot(AppUi.JIMMY_ROOM);
    jimmyController.startChat();
    txtaChat.clear();
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
    txtaChat.clear();
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
    txtaChat.clear();
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
}
