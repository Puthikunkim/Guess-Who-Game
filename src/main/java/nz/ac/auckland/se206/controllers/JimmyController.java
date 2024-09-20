package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;

/**
 * Controller class for the room view. Handles user interactions within the room where the user can
 * chat with customers and guess their profession.
 */
public class JimmyController extends ChatRoomController {

  public static boolean jimmyChatted = false;
  @FXML private Label timerLabel; //

  /**
   * Initializes the room view. If it's the first time initialization, it will provide instructions
   * via text-to-speech.
   */
  @FXML
  public void initialize() {
    character = Characters.JIMMY;
    super.initialize();
  }

  /**
   * Updates the timer label with the given time string.
   *
   * @param timeString the time string to display
   */
  public void updateTimer(String timeString) {
    timerLabel.setText(timeString + "\n" + "Remaining");
  }

  @Override
  @FXML
  protected void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    super.onSendMessage(event);
    jimmyChatted = true;
  }
}
