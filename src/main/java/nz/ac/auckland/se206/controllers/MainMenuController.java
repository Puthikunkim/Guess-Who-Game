package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class MainMenuController extends Controller {
  @FXML private Button playButton;

  @FXML private Button exitButton;

  /**
   * Moves to How to play screen when pressed
   *
   * @param action
   */
  @FXML
  private void onPlayPressed(ActionEvent action) {
    SceneManager.switchRoot(AppUi.HOW_TO_PLAY);
  }

  /**
   * Exits entire game
   *
   * @param action
   */
  @FXML
  private void onExitPressed(ActionEvent action) {
    Platform.exit();
  }
}
