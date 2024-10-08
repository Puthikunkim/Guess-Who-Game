package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class MainMenuController extends Controller {
  @FXML private Button playButton;

  @FXML private Button exitButton;

  private GameStateContext context = RoomController.getContext();

  /**
   * Moves to How to play screen when pressed
   *
   * @param action
   */
  @FXML
  private void onPlayPressed(ActionEvent action) {
    context.playSound("button-4-214382");
    SceneManager.switchRoot(AppUi.HOW_TO_PLAY);
  }

  /**
   * Exits entire game
   *
   * @param action
   */
  @FXML
  private void onExitPressed(ActionEvent action) {
    context.playSound("button-4-214382");
    Platform.exit();
  }
}
