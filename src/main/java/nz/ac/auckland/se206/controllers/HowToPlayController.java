package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** The controller for the how to play menu, when continue the game starts. */
public class HowToPlayController extends Controller {
  private static GameStateContext context;
  @FXML private Button continueButton;

  /**
   * goes to Main Room and starts game.
   *
   * @param event - event of pressing this button
   */
  @FXML
  private void onContinuePressed(ActionEvent event) {
    SceneManager.switchRoot(AppUi.MAIN_ROOM);

    context = RoomController.getContext();
    context.setState(context.getGameStartedState());
    context.playSound("button-4-214382");
  }
}
