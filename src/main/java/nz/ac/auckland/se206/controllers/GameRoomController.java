package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class GameRoomController extends Controller {
  @FXML protected TextArea txtaChat;
  @FXML protected Button btnGuess;
  private GameStateContext context = RoomController.getContext();

  /** when switched to disable button */
  @Override
  public void onSwitchTo() {
    super.onSwitchTo();
    btnGuess.setDisable(true);
    if (SceneManager.getIfCanGuess()) {
      btnGuess.setDisable(false);
    }
  }

  /**
   * Handles the switch button click event to jimmy's scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  protected void onCrimeSceneClick(ActionEvent event) throws IOException {
    SceneManager.switchRoot(AppUi.MAIN_ROOM);
    context.playSound("button-4-214382");
  }

  @FXML
  protected void onJimmyClick(ActionEvent event) throws IOException {
    SceneManager.changeToJimmyScene(event);
    context.playSound("button-4-214382");
  }

  @FXML
  protected void onBusinessmanClick(ActionEvent event) throws IOException {
    SceneManager.changeToBusinessmanScene(event);
    context.playSound("button-4-214382");
  }

  /**
   * Handles the switch button click event to grandma's scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  protected void onGrandmaClick(ActionEvent event) throws IOException {
    SceneManager.changeToGrandmaScene(event);
    context.playSound("button-4-214382");
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  protected void onGuessClick(ActionEvent event) throws IOException {
    SceneManager.changeToGuessScene(event);
    context.playSound("button-4-214382");
  }
}
