package nz.ac.auckland.se206.states;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.Person;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.RoomController;

/**
 * The Guessing state of the game. Handles the logic for when the player is making a guess about the
 * profession of the characters in the game.
 */
public class Guessing implements GameState {

  private final GameStateContext context;

  private Timer timer; // Timer for the current state
  private int timeRemaining;

  /**
   * Constructs a new Guessing state with the given game state context.
   *
   * @param context the context of the game state
   */
  public Guessing(GameStateContext context) {
    this.context = context;
  }

  /**
   * when swithced to change the timer state in room controller to guessing time and switch to main
   * room.
   */
  @Override
  public void onSwitchTo() {
    RoomController roomController = (RoomController) SceneManager.getController(AppUi.MAIN_ROOM);
    SceneManager.switchRoot(AppUi.MAIN_ROOM);
  }

  /** Starts the timer for the game state. */
  @Override
  public void startTimer() {
    timeRemaining = 10; // 10 seconds
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
                  } else {
                    timer.cancel(); // Stop the timer
                    timerExpired(); // Handle the timer expiration
                  }
                }); // Run the task on the JavaFX aaplication thread
          }
        },
        0,
        1000); // Run every second
  }

  /** Stops the timer for the game state. */
  @Override
  public void stopTimer() {
    if (timer != null) {
      timer.cancel();
    }
  }

  /** Updates the timer UI to show the time remaining for the player to make a guess. */
  private void updateTimerDisplay() {
    // Convert the time remaining to minutes and seconds
    int minutes = timeRemaining / 60;
    int seconds = timeRemaining % 60;
    String timeString = String.format("%02d:%02d", minutes, seconds); // Format the time string
    SceneManager.checkTimer(timeString); // Update the timer UI
  }

  /**
   * Handles the event when the timer expires. Notifies the player that the time is up and the game
   * is over and transitions to the game over state.
   */
  private void timerExpired() {
    context.setState(context.getGameOverState());
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
    RoomController roomController = (RoomController) SceneManager.getController(AppUi.MAIN_ROOM);

    // Checks if player guessed correctly, displays appropiate message
    Person clickedPerson = context.getPerson(rectangleId);
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
  public void handleGuessClick() throws IOException {
    RoomController roomController = (RoomController) SceneManager.getController(AppUi.MAIN_ROOM);
  }
}
