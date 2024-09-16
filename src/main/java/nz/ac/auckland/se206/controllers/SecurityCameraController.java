package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SecurityCameraController extends Controller {
  @FXML private ImageView cameraDisplayImage;
  private Image beforeImage = new Image("images/BeforeTheftCamera.png");
  private Image afterImage = new Image("images/AfterTheftCamera.png");
  private Image duringImage = new Image("images/DuringTheftCamera.png");
  private boolean foundTimeOfTheft = false;

  public void onBeforeTimeClick() {
    cameraDisplayImage.setImage(beforeImage);
  }

  public void onAfterTimeClick() {
    cameraDisplayImage.setImage(afterImage);
  }

  /** Correct Time is 2:00 */
  public void onDuringTimeClick() {
    cameraDisplayImage.setImage(duringImage);
    foundTimeOfTheft = true;
  }
}
