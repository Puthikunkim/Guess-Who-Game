package nz.ac.auckland.se206;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.apiproxy.tts.TextToSpeechRequest.Voice;
import nz.ac.auckland.se206.states.GameOver;
import nz.ac.auckland.se206.states.GameStarted;
import nz.ac.auckland.se206.states.GameState;
import nz.ac.auckland.se206.states.Guessing;

/**
 * Context class for managing the state of the game. Handles transitions between different game
 * states and maintains game data such as the professions and rectangle IDs.
 */
public class GameStateContext {

  private final Person personThiefToGuess;
  private final Map<String, Person> rectanglesToPerson;
  private final GameStarted gameStartedState;
  private final Guessing guessingState;
  private final GameOver gameOverState;

  private GameState gameState;
  private boolean chestChecked = false;
  private boolean talkedToPeople = false;
  private boolean guessed = false;
  private MediaPlayer reusedMediaPlayer;

  private Timer timer; // Timer for the current state
  private int timeRemaining;

  /** Constructs a new GameStateContext and initializes the game states and professions. */
  public GameStateContext() {

    gameStartedState = new GameStarted(this);
    guessingState = new Guessing(this);
    gameOverState = new GameOver(this);
    gameState = gameStartedState; // Initial state

    // Randomly chose thief out of the 3 possible
    Random random = new Random();
    random.setSeed(System.currentTimeMillis());
    int randThiefInt = random.nextInt(3);

    // Create array list that stores what index the thief is and what index isnt
    ArrayList<Boolean> thief = new ArrayList<>();
    thief.add(false);
    thief.add(false);
    thief.add(false);
    thief.set(randThiefInt, true);
    // Then take that random choice and put into a map corresponding to each rectID for ez refrence
    // later
    Person bob =
        new Person(
            "Bob",
            thief.get(0),
            Voice.GOOGLE_EN_AU_STANDARD_B,
            "there are cookie crumbs in the chest",
            new Image("images/Crumbs.png"));
    Person skinnyBob =
        new Person(
            "Skinny Bob",
            thief.get(1),
            Voice.GOOGLE_EN_AU_STANDARD_A,
            "there is wheat seeds left in the chest",
            new Image("images/WheatSeeds.png"));
    Person snowmanBob =
        new Person(
            "Snowman Bob",
            thief.get(2),
            Voice.GOOGLE_EN_AU_STANDARD_C,
            "there is water and snowflakes left in the chest",
            new Image("images/SnowAndWater.png"));
    rectanglesToPerson = new HashMap<>();
    rectanglesToPerson.put("rectPerson1", bob);
    rectanglesToPerson.put("rectPerson2", skinnyBob);
    rectanglesToPerson.put("rectPerson3", snowmanBob);

    // Set which rectPerson is the thief for guess checking later
    personThiefToGuess = rectanglesToPerson.get("rectPerson" + (randThiefInt + 1));
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

  /** Starts the timer for the game. */
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
   * plays given sound file
   *
   * @param sound_File_path format "file_name.mp3"
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
   * Gets the ID of the rectangle to be guessed.
   *
   * @return the rectangle ID to guess
   */
  public Person getPersonToGuess() {
    return personThiefToGuess;
  }

  /**
   * Gets the person associated with a specific rectangle ID.
   *
   * @param rectangleId the rectangle ID
   * @return the profession associated with the rectangle ID
   */
  public Person getPerson(String rectangleId) {
    return rectanglesToPerson.get(rectangleId);
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
