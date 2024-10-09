package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** The controller for the securtiy camera scene. */
public class SecurityCameraController extends GameRoomController {

  public static boolean foundTimeOfTheft = false;
  public static boolean firstFound = false;
  @FXML private ImageView cameraDisplayImage;
  @FXML private ImageView staticImageView;
  private Image beforeImage = new Image("images/Vase.png");

  /**
   * If a time before the theft occured is clicked then show an image of the vase still on the
   * pedestal.
   *
   * @param event - event of this button pressed
   */
  @FXML
  private void onBeforeTimeClick(ActionEvent event) {
    cameraDisplayImage.setImage(beforeImage);
    staticImageView.setOpacity(0);
  }

  /**
   * if after time of theft then show vase not on pedestal.
   *
   * @param event - event of this button pressed
   */
  @FXML
  private void onAfterTimeClick(ActionEvent event) {

    cameraDisplayImage.setImage(null);
    staticImageView.setOpacity(0);
  }

  /**
   * Correct Time is 2:00, if correct time is selected show a static screen.
   *
   * @param event - event of this button pressed
   */
  @FXML
  private void onDuringTimeClick(ActionEvent event) {
    // Show static image
    staticImageView.setOpacity(100);
    // On first find set found clue and tell player they have found the clue
    if (!firstFound) {
      txtaChat.appendText("You: Looks like the theft occurred at around 2 o'clock.");
      firstFound = true;
    }
    // after this check if you can guess and update the guessing button
    foundTimeOfTheft = true;
    if (SceneManager.getIfCanGuess()) {
      btnGuess.setDisable(false);
    }
  }

  /**
   * Go back to crime scene.
   *
   * @param event - event of this button pressed
   */
  @FXML
  private void onBackPressed(ActionEvent event) {
    SceneManager.switchRoot(AppUi.MAIN_ROOM);
  }
}
