package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** The controller for the securtiy camera scene. */
public class SecurityCameraController extends GameRoomController {

  public static boolean foundTimeOfTheft = false;
  @FXML private ImageView cameraDisplayImage;
  @FXML private ImageView staticImageView;
  private Image beforeImage = new Image("images/Vase.png");
  private GameStateContext context;

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
    System.out.println(foundTimeOfTheft);
    // On first find set found clue and tell player they have found the clue
    if (!foundTimeOfTheft) {
      txtaChat.appendText("You: Looks like the theft occurred at around 2 o'clock.");
      foundTimeOfTheft = true;
    }
    context.playSound("ping-82822");
  }

  /**
   * Go back to crime scene.
   *
   * @param event - event of this button pressed
   */
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
}
