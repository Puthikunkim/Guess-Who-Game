package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LostAndFoundController extends Controller {

  public static boolean foundCufflink = false;

  @FXML private ImageView thingToDrag;
  @FXML private ImageView cufflink;
  private ImageView dragTarget;
  private Point2D dragMousePointOffset;

  // chat-room
  @FXML private Label timerLabel; //
  @FXML private Button btnCrimeScene;
  @FXML private Button btnJimmy;
  @FXML private Button btnGrandma;
  @FXML private Button btnBusinessman;
  @FXML private Button btnGuess;
  @FXML private TextArea txtaChat;

  @FXML
  public void initialize() {
    cufflink.setOnMouseEntered(
        event -> {
          cufflink.setCursor(Cursor.HAND);
          cufflink.setOpacity(0.5); // Change opacity or add any other visual effect
        });

    cufflink.setOnMouseExited(
        event -> {
          cufflink.setCursor(Cursor.DEFAULT);
          cufflink.setOpacity(1.0); // Reset opacity
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

  @FXML
  public void onDragStart(MouseEvent event) {
    dragTarget = (ImageView) event.getTarget();
    dragMousePointOffset = dragTarget.sceneToLocal(event.getSceneX(), event.getSceneY());
    double offsetX = dragMousePointOffset.getX() - dragTarget.getX();
    double offsetY = dragMousePointOffset.getY() - dragTarget.getY();
    dragMousePointOffset = new Point2D(offsetX, offsetY);
  }

  @FXML
  public void onDragProgressed(MouseEvent event) {
    Scene currentScene = thingToDrag.getScene();

    double newX = event.getSceneX() - dragMousePointOffset.getX();
    double newY = event.getSceneY() - dragMousePointOffset.getY();
    // Clamp Value so cant drag images off screen
    if (newX > 460 - dragTarget.getFitWidth()) {
      newX = 460 - dragTarget.getFitWidth();
    }
    if (newX < 140) {
      newX = 140;
    }

    if (newY > currentScene.getHeight() - 140 - dragTarget.getFitHeight()) {
      newY = currentScene.getHeight() - 140 - dragTarget.getFitHeight();
    }
    if (newY < 160) {
      newY = 160;
    }

    Point2D local = dragTarget.sceneToLocal(newX, newY);

    dragTarget.setX(local.getX());
    dragTarget.setY(local.getY());
  }

  @FXML
  public void onCufflinkClick() {
    // if cufflink is found append text into chat
    foundCufflink = true;
    txtaChat.appendText("You: Hmmm, seems like someone dropped their cufflink.");

    cufflink.setVisible(false);
    cufflink.setDisable(true);

    if (SceneManager.getIfCanGuess()) {
      btnGuess.setDisable(false);
    }
  }

  @FXML
  private void onBackPressed() {
    SceneManager.switchRoot(AppUi.MAIN_ROOM);
  }

  /** when switched to disable button */
  @Override
  public void onSwitchTo() {
    btnGuess.setDisable(true);
    if (SceneManager.getIfCanGuess()) {
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
    RoomController.context.handleGuessClick();
  }
}
