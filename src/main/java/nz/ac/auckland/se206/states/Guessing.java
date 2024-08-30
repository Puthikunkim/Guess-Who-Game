package nz.ac.auckland.se206.states;

import java.io.IOException;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
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
    roomController.guessingMode();
    roomController.getTimerLabel().setText("GUESSING TIME LEFT : " + 10);
    roomController.setInvestigatingTime(0);
    roomController.appendChatMessage(
        new ChatMessage("Narrator", "10 Seconds to Guesssss The Thieeef"));
    context.playSound("10SecondsToGuess");
    SceneManager.switchRoot(AppUi.MAIN_ROOM);
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

    // Cannot accuse the chest
    if (rectangleId.equals("rectChest")) {
      roomController.appendChatMessage(
          new ChatMessage("Narrator", "Please accuse one of the people not the chest"));
      return;
    }

    // Checks if player guessed correctly, displays appropiate message
    Person clickedPerson = context.getPerson(rectangleId);
    if (clickedPerson.getIsThief()) {
      context.playSound("Correct" + clickedPerson.getName());
      roomController.appendChatMessage(
          new ChatMessage(
              "Narrator", "Correct! You won! " + clickedPerson.getName() + " is the thief"));
    } else {
      context.playSound("Incorrect" + clickedPerson.getName());
      roomController.appendChatMessage(
          new ChatMessage(
              "Narrator", "You lost! " + clickedPerson.getName() + " is not the thief"));
    }
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
    roomController.appendChatMessage(new ChatMessage("Narrator", "You are already guessing"));
  }
}
