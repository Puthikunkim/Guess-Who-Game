package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class ReceiptController extends Controller {
  private static GameStateContext context = new GameStateContext();
  @FXML private ImageView receiptImageView;

  @FXML private ImageView receiptPiece1x1;
  @FXML private ImageView receiptPiece1x2;
  @FXML private ImageView receiptPiece2x1;
  @FXML private ImageView receiptPiece2x2;
  @FXML private ImageView receiptPiece3x1;
  @FXML private ImageView receiptPiece3x2;

  @FXML private ImageView[][] receiptPieces;

  @FXML private Button flipButton;

  private boolean frontShowing = true;

  private boolean receiptInfoFound = false;

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
    ImageView[][] tempReceiptPieces = {
      {receiptPiece1x1, receiptPiece1x2},
      {receiptPiece2x1, receiptPiece2x2},
      {receiptPiece3x1, receiptPiece3x2}
    };
    receiptPieces = tempReceiptPieces;
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
    Scene currentScene = receiptImageView.getScene();
    double newX = event.getSceneX() - dragMousePointOffset.getX();
    double newY = event.getSceneY() - dragMousePointOffset.getY();
    // Clamp Value so cant drag images off screen
    if (newX > 599 - dragTarget.getFitWidth()) {
      newX = 599 - dragTarget.getFitWidth();
    }
    if (newX < 0) {
      newX = 0;
    }

    if (newY > currentScene.getHeight() - dragTarget.getFitHeight()) {
      newY = currentScene.getHeight() - dragTarget.getFitHeight();
    }
    if (newY < 0) {
      newY = 0;
    }

    Point2D local = dragTarget.sceneToLocal(newX, newY);

    dragTarget.setX(local.getX());
    dragTarget.setY(local.getY());
    checkPositions();
  }

  private void checkPositions() {
    // Check if each element in a row is next to each other
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 1; j++) {
        if (!checkHorizontal(receiptPieces[i][j], receiptPieces[i][j + 1])) {
          return;
        }
      }
    }

    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        if (!checkVertical(receiptPieces[j][i], receiptPieces[j + 1][i])) {
          return;
        }
      }
    }

    onPuzzleSolve();
  }

  private boolean checkHorizontal(ImageView receiptPieceLeft, ImageView receiptPieceRight) {

    Point2D leftPiecePos =
        receiptPieceLeft.localToScene(receiptPieceLeft.getX(), receiptPieceLeft.getY());
    Point2D rightPiecePos =
        receiptPieceRight.localToScene(receiptPieceRight.getX(), receiptPieceRight.getY());
    // Negative number = left, positive number equal inside or to the right
    double distanceX = leftPiecePos.getX() - rightPiecePos.getX();
    if (!(distanceX > -135 & distanceX < -115)) {
      return false;
    }

    double distanceY = leftPiecePos.getY() - rightPiecePos.getY();

    if (!(distanceY < 10 & distanceY > -10)) {
      return false;
    }
    return true;
  }

  private boolean checkVertical(ImageView receiptPieceUp, ImageView receiptPieceDown) {
    Point2D downPiecePos =
        receiptPieceDown.localToScene(receiptPieceDown.getX(), receiptPieceDown.getY());
    Point2D upPiecePos = receiptPieceUp.localToScene(receiptPieceUp.getX(), receiptPieceUp.getY());
    // Negative number = left, positive number equal inside or to the right
    double distanceX = upPiecePos.getX() - downPiecePos.getX();

    if (!(distanceX > -10 & distanceX < 10)) {
      return false;
    }

    double distanceY = upPiecePos.getY() - downPiecePos.getY();

    if (!(distanceY > -150 & distanceY < -130)) {
      return false;
    }
    return true;
  }

  private void onPuzzleSolve() {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 2; j++) {
        receiptPieces[i][j].setDisable(true);
        receiptPieces[i][j].setOpacity(0);
      }
    }
    receiptImageView.setOpacity(100);
    return;
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
