package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.GameStateContext;

/**
 * Controller class for the room view. Handles user interactions within the room where the user can
 * chat with customers and guess their profession.
 */
public class BusinessmanController extends ChatRoomController {

  public static boolean businessmanChatted = false;
  private GameStateContext context = RoomController.getContext();

  @FXML private Label timerLabel; //

  /**
   * Initializes the room view. If it's the first time initialization, it will provide instructions
   * via text-to-speech.
   */
  @FXML
  public void initialize() {
    character = Characters.BUSINESSMAN;
    super.initialize();
  }

  /**
   * Updates the timer label with the given time string.
   *
   * @param timeString the time string to display
   */
  public void updateTimer(String timeString) {
    // Update the label text
    timerLabel.setText(timeString + "\n" + "Remaining");

    // Split the timeString to extract minutes and seconds
    String[] timeParts = timeString.split(":");
    int minutes = Integer.parseInt(timeParts[0]);
    int seconds = Integer.parseInt(timeParts[1]);

    // Check if there are 10 seconds or less remaining
    if (minutes == 0 && seconds <= 10) {
      timerLabel.setStyle("-fx-text-fill: red;"); // Change text color to red
    } else {
      timerLabel.setStyle(""); // Reset to default style
    }
  }

  /**
   * Updates Businessman chatted for the talked to all 3 suspects check in scene manager.
   *
   * @param event event thrown when send message
   */
  @Override
  @FXML
  protected void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    super.onSendMessage(event);
    businessmanChatted = true;
    Task<Void> backgroundTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            context.playSound("MaleThinking");
            return null;
          }
        };
    Thread backgroundThread = new Thread(backgroundTask);
    backgroundThread.setDaemon(true); // Ensure the thread does not prevent JVM shutdown
    backgroundThread.start();
  }
}
