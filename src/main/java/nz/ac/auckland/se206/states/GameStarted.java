package nz.ac.auckland.se206.states;

import java.io.IOException;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
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
    roomController.getTimerLabel().setText("SECONDS REMAINING : " + 120);
    roomController.startTimer();
    context.playSound("Intro");
    roomController.appendChatMessage(
        new ChatMessage("Narrator", "2 minutes to find the thief who stole your diamonds"));
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
      case "rectChest":
        SceneManager.switchRoot(AppUi.CHEST_ROOM);
        return;
    }

    RoomController roomController = (RoomController) SceneManager.getController(AppUi.MAIN_ROOM);
    roomController.setPerson(context.getPerson(rectangleId));
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
    if (!context.getChestChecked()) {
      context.playSound("NoEvidence");
      roomController.appendChatMessage(
          new ChatMessage("Narrator", "Please find evidence before guessing someone"));
      return;
    } else if (!context.getTalkedToPeople()) {
      context.playSound("HaventTalked");
      roomController.appendChatMessage(
          new ChatMessage("Narrator", "Please talk to the suspect/s before guessing"));
      return;
    }

    context.setState(context.getGuessingState());
  }
}
