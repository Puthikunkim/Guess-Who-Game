package nz.ac.auckland.se206.states;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.GuessingController;

/**
 * The Guessing state of the game. Handles the logic for when the player is making a guess about the
 * profession of the characters in the game.
 */
public class Guessing extends GameState {

  private final GameStateContext context;

  /**
   * Constructs a new Guessing state with the given game state context.
   *
   * @param context the context of the game state
   */
  public Guessing(GameStateContext context) {
    this.context = context;
    this.timerLength = 60;
  }

  /**
   * when swithced to change the timer state in room controller to guessing time and switch to main
   * room.
   */
  @Override
  public void onSwitchTo() {
    SceneManager.switchRoot(AppUi.GUESSING_ROOM);
  }


  /**
   * Handles the event when the timer expires. Notifies the player that the time is up and the game
   * is over and transitions to the game over state.
   */
  @Override
  public void timerExpired() {

    context.setState(context.getGameOverState());
    GuessingController guessingController =
        (GuessingController) SceneManager.getController(AppUi.GUESSING_ROOM);
    if (guessingController.getGuessingCorrect()) {
      try {
        guessingController.onSendMessage(new ActionEvent());
      } catch (Exception e) {
        // TODO: handle exception
      }
    } else {
      SceneManager.switchRoot(AppUi.GAMEOVER_ROOM);
    }
  }

  /**
   * Handles the event when a rectangle is clicked. Checks if the clicked rectangle is a customer
   * and updates the game state accordingly.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @param rectangleId the ID of the clicked rectangle
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleRectangleClick(MouseEvent event, String rectangleId) throws IOException {
    // Checks if player guessed correctly, displays appropiate message
    context.setGuessed(true);
    context.setState(context.getGameOverState());
  }

  /**
   * Handles the event when the guess button is clicked. Since the player has already guessed, it
   * notifies the player.
   *
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleGuessClick() throws IOException {}
}
