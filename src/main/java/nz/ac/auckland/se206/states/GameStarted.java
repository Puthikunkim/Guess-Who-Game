package nz.ac.auckland.se206.states;

import java.io.IOException;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.GuessingController;

/**
 * The GameStarted state of the game. Handles the initial interactions when the game starts,
 * allowing the player to chat with characters and prepare to make a guess.
 */
public class GameStarted extends GameState {

  private final GameStateContext context;

  /**
   * Constructs a new GameStarted state with the given game state context.
   *
   * @param context the context of the game state
   */
  public GameStarted(GameStateContext context) {
    this.context = context;
    this.timerLength = 300;
  }

  /** when game is started start the timer in RoomController. */
  @Override
  public void onSwitchTo() {}

  /** Handles the event when the timer expires. Transitions to the guessing state. */
  public void timerExpired() {
    // switches to guessing room if can guess else to gameover room
    context.setState(context.getGuessingState());
    if (SceneManager.getIfCanGuess()) {
      GuessingController guessingController =
          (GuessingController) SceneManager.getController(AppUi.GUESSING_ROOM);
      SceneManager.switchRoot(AppUi.GUESSING_ROOM);
      guessingController.startChat();
    } else {
      SceneManager.switchRoot(AppUi.GAMEOVER_ROOM);
    }
  }

  /**
   * Handles the event when a rectangle is clicked. Depending on the clicked rectangle, it either
   * provides an introduction or transitions to the chat view.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @param rectangleId the ID of the clicked rectangle
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleRectangleClick(MouseEvent event, String rectangleId) throws IOException {
    // Transition to chat view or provide an introduction based on the clicked rectangle
    switch (rectangleId) {
      case "rectPerson1":
        SceneManager.switchRoot(AppUi.SECURITY_CAMERA_ROOM);
        return;
      case "rectPerson2":
        SceneManager.switchRoot(AppUi.RECEIPT_ROOM);
        return;
      case "rectPerson3":
        SceneManager.switchRoot(AppUi.LOST_FOUND_ROOM);
        return;
    }
    context.setTalkedToPeople(true);
  }

  /**
   * Handles the event when the guess button is clicked. Prompts the player to make a guess and
   * transitions to the guessing state.
   *
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleGuessClick() throws IOException {
    context.setState(context.getGuessingState());
  }
}
