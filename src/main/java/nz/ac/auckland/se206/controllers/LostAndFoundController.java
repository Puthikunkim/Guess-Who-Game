package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class LostAndFoundController extends Controller {
  @FXML private ImageView thingToDrag;
  @FXML private ImageView cufflink;
  private ImageView dragTarget;
  private double dragStartMouseX = 0;
  private double dragStartMouseY = 0;
  private boolean foundCufflink = false;

  @FXML
  public void onDragStart(MouseEvent event) {
    dragTarget = (ImageView) event.getTarget();
    dragStartMouseX = event.getSceneX();
    dragStartMouseY = event.getSceneY();
  }

  @FXML
  public void onDragProgressed(MouseEvent event) {
    Scene currentScene = thingToDrag.getScene();
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

  @FXML
  public void onCufflinkClick() {
    foundCufflink = true;
  }
}
