package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;

/** Controller for the game over scene, Handles Replay and Exit. */
public class GameOverController extends Controller {
  @FXML private Button playAgainBtn;
  @FXML private Button exitBtn;
  private GameStateContext context = RoomController.getContext();

  /**
   * goes to Main Room and restarts game.
   *
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onPlayAgainPressed(ActionEvent action) throws IOException {
    context.playSound("button-4-214382");
    SceneManager.restart();
  }

  /** Handles exit button press closing the entire game. */
  @FXML
  private void onExitPressed(ActionEvent action) {
    context.playSound("button-4-214382");
    Platform.exit();
  }
}
