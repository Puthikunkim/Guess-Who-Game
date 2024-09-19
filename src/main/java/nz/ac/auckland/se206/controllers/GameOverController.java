package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;

public class GameOverController extends Controller {
  private static GameStateContext context;
  @FXML private Button playAgainBtn;
  @FXML private Button exitBtn;

  /**
   * goes to Main Room and restarts game
   *
   * @param action
   * @throws IOException
   */
  @FXML
  private void onPlayAgainPressed(ActionEvent action) throws IOException {
    SceneManager.restart();
  }

  /**
   * exits game
   *
   * @param action
   */
  @FXML
  private void onExitPressed(ActionEvent action) {
    Platform.exit();
  }
}