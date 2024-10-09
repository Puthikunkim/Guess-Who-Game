package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** controller for the main menu, players can either exit or go to how to play menu */
public class MainMenuController extends Controller {
  @FXML private Button playButton;

  @FXML private Button exitButton;

  /**
   * Moves to How to play screen when pressed.
   *
   * @param event - event of this button pressed
   */
  @FXML
  private void onPlayPressed(ActionEvent event) {
    SceneManager.switchRoot(AppUi.HOW_TO_PLAY);
  }

  /**
   * when pressing button you exit the game by closing th game window
   *
   * @param event - event of pressing this button
   */
  @FXML
  private void onExitPressed(ActionEvent event) {
    Platform.exit();
  }
}
