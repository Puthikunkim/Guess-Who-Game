package nz.ac.auckland.se206;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.states.GameOver;
import nz.ac.auckland.se206.states.GameStarted;
import nz.ac.auckland.se206.states.GameState;
import nz.ac.auckland.se206.states.Guessing;

/**
 * Context class for managing the state of the game. Handles transitions between different game
 * states and maintains game data such as the professions and rectangle IDs.
 */
public class GameStateContext {

  private final GameStarted gameStartedState;
  private final Guessing guessingState;
  private final GameOver gameOverState;

  private GameState gameState;
  private boolean chestChecked = false;
  private boolean talkedToPeople = false;
  private boolean guessed = false;
  private MediaPlayer reusedMediaPlayer;
  private MediaPlayer reusedTTSPlayer;

  private Timer timer; // Timer for the current state
  private int timeRemaining;

  /** Constructs a new GameStateContext and initializes the game states and professions. */
  public GameStateContext() {
    gameStartedState = new GameStarted(this);
    guessingState = new Guessing(this);
    gameOverState = new GameOver(this);
    gameState = gameStartedState; // Initial state
  }

  /**
   * sets ChestChecked to boolean.
   *
   * @param chestChecked boolean
   */
  public void setChestChecked(boolean chestChecked) {
    this.chestChecked = chestChecked;
  }

  public boolean getChestChecked() {
    return chestChecked;
  }

  public void setTalkedToPeople(boolean talkedToPeople) {
    this.talkedToPeople = talkedToPeople;
  }

  public boolean getTalkedToPeople() {
    return talkedToPeople;
  }

  public void setGuessed(boolean guessed) {
    this.guessed = guessed;
  }

  public boolean getGuessed() {
    return guessed;
  }

  /**
   * Starts the timer with the given time.
   *
   * @param time - how long timer should be set for
   */
  public void startTimer(int time) {
    timeRemaining = time; // 2 minutes in seconds
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
                    gameState.timerExpired(); // Handle the timer expiration
                  }
                }); // Run the task on the JavaFX application thread
          }
        },
        0,
        1000); // Run every second
  }

  /** Stops the timer for the current game state. */
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

  /**
   * plays given sound file.
   *
   * @param soundFileName format "file_name.mp3"
   */
  public void playSound(String soundFileName) {
    try {
      Media newSound =
          new Media(App.class.getResource("/sounds/" + soundFileName + ".mp3").toURI().toString());
      // reusedMediaPlayer.stop();
      reusedMediaPlayer = new MediaPlayer(newSound);
      reusedMediaPlayer.play();
    } catch (URISyntaxException e) {
      System.out.println(soundFileName);
      System.out.println(e.getStackTrace());
    }
  }

  /**
   * plays given sound file
   *
   * @param sound_File_path format "file_name.mp3"
   */
  public void playSoundTTS(String soundFileName) {
    try {
      Media newSound =
          new Media(App.class.getResource("/sounds/" + soundFileName + ".mp3").toURI().toString());
      reusedTTSPlayer = new MediaPlayer(newSound);
      reusedTTSPlayer.play();
    } catch (URISyntaxException e) {
      System.out.println(soundFileName);
      System.out.println(e.getStackTrace());
    }
  }

  /**
   * Sets the current state of the game.
   *
   * @param state the new state to set
   */
  public void setState(GameState state) {
    stopTimer(); // Stop the timer for the current state
    this.gameState = state;
    startTimer(gameState.getTimerLength()); // Start the timer for the new state
    gameState.onSwitchTo();
  }

  /**
   * gets Current GameState.
   *
   * @return GameState
   */
  public GameState getState() {
    return gameState;
  }

  /**
   * Gets the initial game started state.
   *
   * @return the game started state
   */
  public GameState getGameStartedState() {
    return gameStartedState;
  }

  /**
   * Gets the guessing state.
   *
   * @return the guessing state
   */
  public GameState getGuessingState() {
    return guessingState;
  }

  /**
   * Gets the game over state.
   *
   * @return the game over state
   */
  public GameState getGameOverState() {
    return gameOverState;
  }

  /**
   * Handles the event when a rectangle is clicked.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @param rectangleId the ID of the clicked rectangle
   * @throws IOException if there is an I/O error
   */
  public void handleRectangleClick(MouseEvent event, String rectangleId) throws IOException {
    gameState.handleRectangleClick(event, rectangleId);
  }

  /**
   * Handles the event when the guess button is clicked.
   *
   * @throws IOException if there is an I/O error
   */
  public void handleGuessClick() throws IOException {
    gameState.handleGuessClick();
  }
}
