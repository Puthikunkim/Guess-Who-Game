package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class ReceiptController extends GameRoomController {

  public static boolean receiptInfoFound = false;
  @FXML private ImageView receiptImageView;

  @FXML private ImageView receiptPiece1x1;
  @FXML private ImageView receiptPiece1x2;
  @FXML private ImageView receiptPiece1x3;
  @FXML private ImageView receiptPiece2x1;
  @FXML private ImageView receiptPiece2x2;
  @FXML private ImageView receiptPiece2x3;
  @FXML private ImageView receiptPiece3x1;
  @FXML private ImageView receiptPiece3x2;
  @FXML private ImageView receiptPiece3x3;

  @FXML private ImageView[][] receiptPieces;

  @FXML private Button flipButton;

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
    // Initialize the receipt pieces
    ImageView[][] tempReceiptPieces = {
      {receiptPiece1x1, receiptPiece1x2, receiptPiece1x3},
      {receiptPiece2x1, receiptPiece2x2, receiptPiece2x3},
      {receiptPiece3x1, receiptPiece3x2, receiptPiece3x3}
    };
    receiptPieces = tempReceiptPieces;
  }

  /**
   * Updates the timer label with the given time string.
   *
   * @param timeString the time string to display
   */
  public void updateTimer(String timeString) {
    // Update the label text
    timerLabel.setText(timeString + "\n" + "Remaining");

    // Split the timeString to extract minutes and seconds
    String[] timeParts = timeString.split(":");
    int minutes = Integer.parseInt(timeParts[0]);
    int seconds = Integer.parseInt(timeParts[1]);

    // Check if there are 10 seconds or less remaining
    if (minutes == 0 && seconds <= 10) {
      timerLabel.setStyle("-fx-text-fill: red;"); // Change text color to red
    } else {
      timerLabel.setStyle(""); // Reset to default style
    }
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
      for (int j = 0; j < 2; j++) {
        if (!checkHorizontal(receiptPieces[i][j], receiptPieces[i][j + 1])) {
          return;
        }
      }
    }

    for (int i = 0; i < 3; i++) {
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
    if (!(distanceX > -120 & distanceX < -100)) {
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
    // put the pieces together once the puzzle is solved
    if (receiptInfoFound) {
      return;
    }
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        receiptPieces[i][j].setDisable(true);
        receiptPieces[i][j].setOpacity(0);
      }
    }
    receiptImageView.setOpacity(100);
    txtaChat.appendText(
        "You: Someone purchased a very expensive protective casing, I wonder what they need it"
            + " for...");
    receiptInfoFound = true;
    if (SceneManager.getIfCanGuess()) {
      btnGuess.setDisable(false);
    }
  }

  @FXML
  private void onBackPressed() {
    SceneManager.switchRoot(AppUi.MAIN_ROOM);
  }
}
