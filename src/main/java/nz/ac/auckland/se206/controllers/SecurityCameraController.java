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

  public static boolean foundTimeOfTheft = false;
  @FXML private ImageView cameraDisplayImage;
  @FXML private ImageView staticImageView;
  private Image beforeImage = new Image("images/Vase.png");

  // chat-room
  @FXML private Label timerLabel; //
  @FXML private Button btnCrimeScene;
  @FXML private Button btnJimmy;
  @FXML private Button btnGrandma;
  @FXML private Button btnBusinessman;
  @FXML private Button btnGuess;
  @FXML private TextArea txtaChat;

  private GameStateContext context;

  /**
   * Updates the timer label with the given time string.
   *
   * @param timeString the time string to display
   */
  public void updateTimer(String timeString) {
    timerLabel.setText(timeString + "\n" + "Remaining");
  }

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
    txtaChat.appendText("You: Looks like the theft occurred at around 2 o'clock.");

    foundTimeOfTheft = true;
    if (SceneManager.getIfCanGuess()) {
      btnGuess.setDisable(false);
    }
    context.playSound("ping-82822");
  }

  @FXML
  private void onBackPressed() {
    SceneManager.switchRoot(AppUi.MAIN_ROOM);
  }

  /** when switched to disable button */
  @Override
  public void onSwitchTo() {
    context = RoomController.getContext();
    btnGuess.setDisable(true);
    if (SceneManager.getIfCanGuess()) {
      btnGuess.setDisable(false);
    }
    context.playSound("ping-82822");
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
    txtaChat.clear();
  }

  @FXML
  private void onJimmyClick(ActionEvent event) throws IOException {
    SceneManager.changeToJimmyScene(event);
    txtaChat.clear();
  }

  /**
   * Handles the switch button click event to grandma's scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGrandmaClick(ActionEvent event) throws IOException {
    SceneManager.changeToGrandmaScene(event);
  }

  /**
   * Handles the switch button click event to grandma's scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onBusinessmanClick(ActionEvent event) throws IOException {
    SceneManager.changeToBusinessmanScene(event);
    txtaChat.clear();
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGuessClick(ActionEvent event) throws IOException {
    context.playSound("button-4-214382");
    SceneManager.changeToGuessScene(event);
  }
}
