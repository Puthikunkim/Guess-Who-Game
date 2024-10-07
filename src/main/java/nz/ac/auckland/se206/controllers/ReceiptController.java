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

/** Controller for the receipt puzzle scene */
public class ReceiptController extends GameRoomController {

  public static boolean receiptInfoFound = false;
  @FXML private ImageView receiptImageView;
  // All the recipt pieces (X x Y)
  @FXML private ImageView receiptPiece1x1;
  @FXML private ImageView receiptPiece1x2;
  @FXML private ImageView receiptPiece1x3;
  @FXML private ImageView receiptPiece2x1;
  @FXML private ImageView receiptPiece2x2;
  @FXML private ImageView receiptPiece2x3;
  @FXML private ImageView receiptPiece3x1;
  @FXML private ImageView receiptPiece3x2;
  @FXML private ImageView receiptPiece3x3;

  // Store recipt pieces in a 2D array for easy comparison
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
    // Initialize the receipt pieces, for easy comparison
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

  /**
   * When starting drag initialize where the player has grabbed the object from compare to the
   * centre of the object.
   *
   * @param event - mouse event that stores position of the mouse cursor and object being dragged
   */
  @FXML
  public void onDragStart(MouseEvent event) {
    dragTarget = (ImageView) event.getTarget();
    dragMousePointOffset = dragTarget.sceneToLocal(event.getSceneX(), event.getSceneY());
    double offsetX = dragMousePointOffset.getX() - dragTarget.getX();
    double offsetY = dragMousePointOffset.getY() - dragTarget.getY();
    dragMousePointOffset = new Point2D(offsetX, offsetY);
  }

  /**
   * Moves the dragged object by the same amount and distance as the mouse, this maintains the
   * offset of when the player clicked the first time.
   *
   * @param event - mouse event that contains the new mouse position after moving
   */
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

  /**
   * Every time a piece is moved, check all positions and their neighbours to see if they are in a
   * valid configuration
   */
  private void checkPositions() {
    // Check if each element in a row is next to each other
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 2; j++) {
        if (!checkHorizontal(receiptPieces[i][j], receiptPieces[i][j + 1])) {
          return;
        }
      }
    }
    // Check if pieces are in the correct columns
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 2; j++) {
        if (!checkVertical(receiptPieces[j][i], receiptPieces[j + 1][i])) {
          return;
        }
      }
    }

    onPuzzleSolve();
  }

  /**
   * Checks if the right piece is physically to the right of the left pience
   *
   * @param receiptPieceLeft
   * @param receiptPieceRight
   * @return
   */
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

  /**
   * Checks if the up piece is almost direclty above the down piece.
   *
   * @param receiptPieceUp
   * @param receiptPieceDown
   * @return
   */
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

  /**
   * When puzzle is solved we hide all the seperate pieces and show the image of the whole, also
   * tell them they found a clue
   */
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
