package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Controller for the lost and found scene, where the cufflink clue is found */
public class LostAndFoundController extends GameRoomController {

  public static boolean foundCufflink = false;

  //
  @FXML private ImageView thingToDrag;
  @FXML private ImageView cufflink;
  private ImageView dragTarget;
  private Point2D dragMousePointOffset;

  /** When initialzed set functions so that the cufflink interaction is more obvious. */
  @FXML
  public void initialize() {
    // On mouse hover on the vufflink chang its opacity and set the cursor to a closed hand
    cufflink.setOnMouseEntered(
        event -> {
          cufflink.setCursor(Cursor.CLOSED_HAND);
          cufflink.setOpacity(0.5); // Change opacity or add any other visual effect
        });

    cufflink.setOnMouseExited(
        event -> {
          cufflink.setCursor(Cursor.DEFAULT);
          cufflink.setOpacity(1.0); // Reset opacity
        });
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

    Scene currentScene = thingToDrag.getScene();
    // Get the current mouse position and remove the offset calculated earlier to get the new
    // position of the object being dragged
    double newX = event.getSceneX() - dragMousePointOffset.getX();
    double newY = event.getSceneY() - dragMousePointOffset.getY();
    // Clamp Values so cant drag images off screen
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

    // Switch from scene position to local position
    Point2D local = dragTarget.sceneToLocal(newX, newY);

    dragTarget.setX(local.getX());
    dragTarget.setY(local.getY());
  }

  /**
   * when cufflink is clicked hide cufflink and inform user that they have found a clue.
   *
   * @param event - event when cufflink is clicked
   */
  @FXML
  public void onCufflinkClick(MouseEvent event) {
    // if cufflink is found append text into chat
    foundCufflink = true;
    txtaChat.appendText("You: Hmmm, seems like someone dropped their cufflink.");

    cufflink.setVisible(false);
    cufflink.setDisable(true);

    if (SceneManager.getIfCanGuess()) {
      btnGuess.setDisable(false);
    }
  }

  /**
   * Goes back to crime scene.
   *
   * @param event - event of pressing this button
   */
  @FXML
  private void onBackPressed(ActionEvent event) {
    SceneManager.switchRoot(AppUi.MAIN_ROOM);
  }
}
