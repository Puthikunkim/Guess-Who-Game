package nz.ac.auckland.se206.states;

import java.io.IOException;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.Person;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.RoomController;

/**
 * The GameOver state of the game. Handles interactions after the game has ended, informing the
 * player that the game is over and no further actions can be taken.
 */
public class GameOver implements GameState {

  private final GameStateContext context;

  /**
   * Constructs a new GameOver state with the given game state context.
   *
   * @param context the context of the game state
   */
  public GameOver(GameStateContext context) {
    this.context = context;
  }

  /** When Switched to disable everything and set GAME OVER */
  public void onSwitchTo() {}

  /**
   * Handles the event when a rectangle is clicked. Informs the player that the game is over and
   * provides the profession of the clicked character if applicable.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @param rectangleId the ID of the clicked rectangle
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleRectangleClick(MouseEvent event, String rectangleId) throws IOException {
    if (rectangleId.equals("rectChest")) {
      return;
    }
    Person clickedPerson = context.getPerson(rectangleId);
    RoomController roomController = (RoomController) SceneManager.getController(AppUi.MAIN_ROOM);
    context.playSound("Thief" + context.getPersonToGuess().getName());
  }

  /**
   * Handles the event when the guess button is clicked. Informs the player that the game is over
   * and no further guesses can be made.
   *
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleGuessClick() throws IOException {
    RoomController roomController = (RoomController) SceneManager.getController(AppUi.MAIN_ROOM);
  }

  /** Does nothing as the game is over and no further actions can be taken. */
  @Override
  public void startTimer() {
    // Do nothing
  }

  /** Does nothing as the game is over and no further actions can be taken. */
  @Override
  public void stopTimer() {
    // Do nothing
  }
}
