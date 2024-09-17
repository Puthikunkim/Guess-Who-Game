package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ReceiptController extends Controller {
  @FXML private ImageView receiptImageView;

  @FXML private ImageView receiptPiece1x1;
  @FXML private ImageView receiptPiece1x2;
  @FXML private ImageView receiptPiece2x1;
  @FXML private ImageView receiptPiece2x2;
  @FXML private ImageView receiptPiece3x1;
  @FXML private ImageView receiptPiece3x2;

  @FXML private ImageView[][] receiptPieces;

  private Image frontReceipt = new Image("images/ReceiptFront.png");
  private Image backReceipt = new Image("images/ReceiptBack.png");
  private boolean frontShowing = true;

  private boolean receiptInfoFound = false;

  private ImageView dragTarget;
  private double dragStartMouseX = 0;
  private double dragStartMouseY = 0;
  private boolean puzzleSolved = false;

  @FXML
  public void initialize() {
    ImageView[][] tempReceiptPieces = {
      {receiptPiece1x1, receiptPiece2x1, receiptPiece3x1},
      {receiptPiece1x2, receiptPiece2x2, receiptPiece3x2},
    };
    receiptPieces = tempReceiptPieces;
  }

  @Override
  public void onSwitchTo() {
    // Random rand = new Random();
    // for (int i = 0; i < 2; i++) {
    //   for (int j = 0; j < 3; j++) {
    //     int sceneWidth = (int) receiptImageView.getScene().getWidth();
    //     int RandomX = rand.nextInt(sceneWidth - 130) - sceneWidth / 2 + 130;
    //     int sceneHeight = (int) receiptImageView.getScene().getHeight();
    //     int RandomY = rand.nextInt(sceneHeight - 130) - sceneHeight / 2 + 130;
    //     receiptPieces[i][j].setX(RandomX);
    //     receiptPieces[i][j].setY(RandomY);
    //   }
    // }
  }

  @FXML
  public void onFlip() {
    if (frontShowing) {
      receiptImageView.setImage(backReceipt);
      frontShowing = false;
    } else {
      receiptImageView.setImage(frontReceipt);
      frontShowing = true;
      receiptInfoFound = true;
    }
  }

  @FXML
  public void onDragStart(MouseEvent event) {
    dragTarget = (ImageView) event.getTarget();
    dragStartMouseX = event.getSceneX();
    dragStartMouseY = event.getSceneY();
  }

  @FXML
  public void onDragProgressed(MouseEvent event) {
    Scene currentScene = receiptImageView.getScene();
    double newX = event.getSceneX();
    double newY = event.getSceneY();
    // Clamp Value so cant drag images off screen
    if (event.getSceneX() > currentScene.getWidth()) {
      newX = currentScene.getWidth();
    }
    if (event.getSceneX() < 0) {
      newX = 0;
    }

    if (event.getSceneY() > currentScene.getHeight()) {
      newY = currentScene.getHeight();
    }
    if (event.getSceneY() < 0) {
      newY = 0;
    }
    dragTarget.setX(dragTarget.getX() - (dragStartMouseX - newX));
    dragStartMouseX = newX;
    dragTarget.setY(dragTarget.getY() - (dragStartMouseY - newY));
    dragStartMouseY = newY;
    checkPositions();
  }

  private void checkPositions() {
    // Check if each element in a row is next to each other
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        // System.out.println(receiptPieces[i][j].getId() + ":" + receiptPieces[i][j].getLayoutX());
        if (!checkHorizontal(receiptPieces[i][j], receiptPieces[i][j + 1])) {
          // System.out.println(receiptPieces[i][j].getId() + ":" + receiptPieces[i][j].getX());
          System.out.println(receiptPieces[i][j].getId() + " NOT IN HORIZONTAL POSITION");
          return;
        }
      }
    }

    for (int i = 0; i < 3; i++) {
      if (!checkVertical(receiptPieces[0][i], receiptPieces[1][i])) {
        System.out.println(receiptPieces[0][i].getId() + " NOT IN VERTICAL POSITION");
        return;
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
    System.out.println(distanceX);
    if (!(distanceX > -125 & distanceX < -105)) {
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

    if (!(distanceY > -121.5 & distanceY < -101.5)) {
      return false;
    }
    return true;
  }

  private void onPuzzleSolve() {
    System.out.println("SOLVED");
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 3; j++) {
        receiptPieces[i][j].setDisable(true);
      }
    }
    return;
  }
}
