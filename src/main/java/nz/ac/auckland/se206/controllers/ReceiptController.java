package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
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

  private Image frontReceipt = new Image("images/ReceiptFront.png");
  private Image backReceipt = new Image("images/ReceiptBack.png");
  private boolean frontShowing = true;
  private boolean receiptInfoFound = false;

  private ImageView dragTarget;
  private double dragStartMouseX = 0;
  private double dragStartMouseY = 0;

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
  }
}
