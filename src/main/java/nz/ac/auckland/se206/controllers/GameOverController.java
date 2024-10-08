package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;

public class GameOverController extends Controller {
  @FXML private Button playAgainBtn;
  @FXML private Button exitBtn;
  private GameStateContext context = RoomController.getContext();

  /**
   * goes to Main Room and restarts game
   *
   * @param action
   * @throws IOException
   */
  @FXML
  private void onPlayAgainPressed(ActionEvent action) throws IOException {
    context.playSound("button-4-214382");
    SceneManager.restart();
  }

  /**
   * exits game
   *
   * @param action
   */
  @FXML
  private void onExitPressed(ActionEvent action) {
    context.playSound("button-4-214382");
    Platform.exit();
  }
}
