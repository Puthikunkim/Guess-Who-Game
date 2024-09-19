package nz.ac.auckland.se206.states;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.RoomController;

/**
 * The GameStarted state of the game. Handles the initial interactions when the game starts,
 * allowing the player to chat with characters and prepare to make a guess.
 */
public class GameStarted implements GameState {

  private final GameStateContext context;

  private Timer timer; // Timer for the current state
  private int timeRemaining;

  /**
   * Constructs a new GameStarted state with the given game state context.
   *
   * @param context the context of the game state
   */
  public GameStarted(GameStateContext context) {
    this.context = context;
  }

  /** when game is started start the timer in RoomController. */
  @Override
  public void onSwitchTo() {
    RoomController roomController = (RoomController) SceneManager.getController(AppUi.MAIN_ROOM);
  }

  /** Starts the timer for the game. */
  @Override
  public void startTimer() {
    timeRemaining = 300; // 2 minutes in seconds
    timer = new Timer(); // Create a new timer
    // Schedule a task to run every second
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            Platform.runLater(
                () -> {
                  if (timeRemaining >= 0) {
                    updateTimerDisplay(); // Update the timer UI
                    timeRemaining--; // Decrement the time remaining
                  } else { // If the time is up
                    timer.cancel(); // Cancel the timer
                    timerExpired(); // Handle the timer expiration
                  }
                }); // Run the task on the JavaFX application thread
          }
        },
        0,
        1000); // Run every second
  }

  /** Stops the timer for the current game state. */
  @Override
  public void stopTimer() {
    if (timer != null) {
      timer.cancel();
    }
  }

  /** Updates the timer UI to show the time remaining in the game. */
  private void updateTimerDisplay() {
    // Convert the time remaining to minutes and seconds
    int minutes = timeRemaining / 60;
    int seconds = timeRemaining % 60;
    String timeString = String.format("%02d:%02d", minutes, seconds); // Format the time string
    SceneManager.checkTimer(timeString); // Update the timer UI
  }

  /** Handles the event when the timer expires. Transitions to the guessing state. */
  private void timerExpired() {
    context.setState(context.getGuessingState());
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

    RoomController roomController = (RoomController) SceneManager.getController(AppUi.MAIN_ROOM);
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
    RoomController roomController = (RoomController) SceneManager.getController(AppUi.MAIN_ROOM);
    context.setState(context.getGuessingState());
  }
}
