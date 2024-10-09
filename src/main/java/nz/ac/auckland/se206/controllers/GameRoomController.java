package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/**
 * Parent class for all Room controllers that exist within the game itself, excluding the Start,How
 * to play and Game Over scenes
 */
public class GameRoomController extends Controller {
  @FXML protected TextArea txtaChat;
  @FXML protected Button btnGuess;

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
   * Handles the switch button click event to crime scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  protected void onCrimeSceneClick(ActionEvent event) throws IOException {
    SceneManager.switchRoot(AppUi.MAIN_ROOM);
  }

  /**
   * Handles the switch button click event to jimmy's scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  protected void onJimmyClick(ActionEvent event) throws IOException {
    SceneManager.changeToJimmyScene(event);
  }

  /**
   * Handles the switch button click event to business man scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  protected void onBusinessmanClick(ActionEvent event) throws IOException {
    SceneManager.changeToBusinessmanScene(event);
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
  }

  /**
   * Handles the guess button click event., Switches to guessing scene
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  protected void onGuessClick(ActionEvent event) throws IOException {
    SceneManager.changeToGuessScene(event);
  }
}