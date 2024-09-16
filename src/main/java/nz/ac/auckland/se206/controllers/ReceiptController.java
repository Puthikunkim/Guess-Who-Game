package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ReceiptController extends Controller {
  @FXML private ImageView receiptImageView;
  private Image frontReceipt = new Image("images/ReceiptFront.png");
  private Image backReceipt = new Image("images/ReceiptBack.png");
  private boolean frontShowing = true;
  private boolean receiptInfoFound = false;

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
}
