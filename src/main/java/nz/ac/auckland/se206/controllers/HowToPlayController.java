package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class HowToPlayController extends Controller {
  @FXML private Button continueButton;

  /**
   * goes to Main Room and starts game
   *
   * @param action
   */
  @FXML
  private void onContinuePressed(ActionEvent action) {
    SceneManager.switchRoot(AppUi.MAIN_ROOM);
  }
}