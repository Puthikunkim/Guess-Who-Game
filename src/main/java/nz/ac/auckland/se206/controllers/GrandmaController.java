package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;

/**
 * Controller class for the grandma scene. Handles user interactions within the room where the user
 * can chat with grandma
 */
public class GrandmaController extends ChatRoomController {

  public static boolean grandmaChatted = false;

  /**
   * Initializes the room view. If it's the first time initialization, it will provide instructions
   * via text-to-speech.
   */
  @FXML
  public void initialize() {
    character = Characters.GRANDMA;
    super.initialize();
  }

  /**
   * Updates grandma chatted for the {talked to all 3 suspects} check in scene manager.
   *
   * @param event event thrown when send message
   */
  @Override
  @FXML
  protected void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    super.onSendMessage(event);
    grandmaChatted = true;
  }
}
