package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.controllers.BusinessmanController;
import nz.ac.auckland.se206.controllers.Controller;
import nz.ac.auckland.se206.controllers.GrandmaController;
import nz.ac.auckland.se206.controllers.GuessingController;
import nz.ac.auckland.se206.controllers.JimmyController;
import nz.ac.auckland.se206.controllers.LostAndFoundController;
import nz.ac.auckland.se206.controllers.ReceiptController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.controllers.SecurityCameraController;

public class SceneManager {

  /** Enum that corresponds to each room and its controller. */
  public enum AppUi {
    MAIN_MENU,
    HOW_TO_PLAY,
    MAIN_ROOM,
    JIMMY_ROOM,
    GRANDMA_ROOM,
    BUSINESSMAN_ROOM,
    GUESSING_ROOM,
    SECURITY_CAMERA_ROOM,
    LOST_FOUND_ROOM,
    RECEIPT_ROOM,
    GAMEOVER_ROOM,
  }

  private static Scene scene;
  private static Stage stage;
  // Maps containing the room and controller corresponding to the AppUI enum
  private static Map<AppUi, Parent> sceneMap;
  private static Map<AppUi, Controller> controllerMap;

  /**
   * Runs on application start(called in App.start), to initialaze all rooms and their controllers.
   *
   * @param stage - start stage
   * @throws IOException - if there is an I/O error
   */
  public static void start(final Stage stage) throws IOException {

    JimmyController.jimmyChatted = false;
    GrandmaController.grandmaChatted = false;
    BusinessmanController.businessmanChatted = false;

    LostAndFoundController.foundCufflink = false;
    SecurityCameraController.foundTimeOfTheft = false;
    ReceiptController.receiptInfoFound = false;

    sceneMap = new HashMap<>();
    controllerMap = new HashMap<>();
    // Save refrence to stage for easy resizing later
    SceneManager.stage = stage;
    // Load All Rooms for easy switching
    SceneManager.addUi(AppUi.MAIN_MENU, "mainMenu");
    SceneManager.addUi(AppUi.HOW_TO_PLAY, "howToPlay");
    SceneManager.addUi(AppUi.MAIN_ROOM, "room");

    // chat-room
    SceneManager.addUi(AppUi.JIMMY_ROOM, "jimmy");
    SceneManager.addUi(AppUi.GRANDMA_ROOM, "grandma");
    SceneManager.addUi(AppUi.BUSINESSMAN_ROOM, "businessman");

    // guessing-room
    SceneManager.addUi(AppUi.GUESSING_ROOM, "guessing");
    SceneManager.addUi(AppUi.SECURITY_CAMERA_ROOM, "securityCamera");
    SceneManager.addUi(AppUi.LOST_FOUND_ROOM, "lostAndFound");
    SceneManager.addUi(AppUi.RECEIPT_ROOM, "receipt");

    // game over
    SceneManager.addUi(AppUi.GAMEOVER_ROOM, "gameOver");

    // Set initial room to MainMenu.fxml
    Parent mainRoom = SceneManager.getRoot(AppUi.MAIN_MENU);
    scene = new Scene(mainRoom);
    scene
        .getStylesheets()
        .add(App.class.getResource("/css/style.css").toExternalForm()); // Load CSS here
    stage.setScene(scene);
    stage.show();
    mainRoom.requestFocus();
  }

  /**
   * Restarts the game by reloading all scenes.
   *
   * @throws IOException if there is an I/O error
   */
  public static void restart() throws IOException {
    start(stage);
  }

  /**
   * Loads chosen FXML and saves its root and controller to the chosen AppUi.
   *
   * @param appUi - AppUi enum that corresponds to the room the user wants to add
   * @param fxml - Path to the FXML corresponding to chosen room
   * @throws IOException - if there is an I/O error
   */
  public static void addUi(AppUi appUi, String fxml) throws IOException {
    // Loads FXML
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    // Get root and controller of FXML
    Parent root = fxmlLoader.load();
    Controller controller = fxmlLoader.getController();
    // Save Controller and root to corresponding map
    sceneMap.putIfAbsent(appUi, root);
    controllerMap.putIfAbsent(appUi, controller);
  }

  /**
   * gets root for the given AppUi.
   *
   * @param appUi - AppUi enum that corresponds to the room the user wants to get root of
   * @return
   */
  public static Parent getRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }

  /**
   * gets controller for the given AppUi.
   *
   * @param appUi - the enum AppUi that corresponds to the room controller wanted
   * @return
   */
  public static Controller getController(AppUi appUi) {
    return controllerMap.get(appUi);
  }

  /**
   * Switch scene root to target AppUi scene.
   *
   * @param targetScene - AppUi enum that corresponds to the room the user wants to switch to
   */
  public static void switchRoot(AppUi targetScene) {
    try {
      // Set root of given appui to the scene root
      scene.setRoot(getRoot(targetScene));
      // Set stage scene and resize the stage(window)
      stage.sizeToScene();
      // Call onSwitchTo to make sure logic that occurs on swithing ot scene happens
      getController(targetScene).onSwitchTo();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Updates all rooms that have timer with current timer.
   *
   * @param timeString - Time to update all the visual timers with
   */
  public static void checkTimer(String timeString) {
    // get controller for all rooms
    RoomController roomController = (RoomController) getController(AppUi.MAIN_ROOM);
    JimmyController jimmyController = (JimmyController) getController(AppUi.JIMMY_ROOM);
    GrandmaController grandmaController = (GrandmaController) getController(AppUi.GRANDMA_ROOM);
    BusinessmanController businessmanController =
        (BusinessmanController) getController(AppUi.BUSINESSMAN_ROOM);

    GuessingController guessingController = (GuessingController) getController(AppUi.GUESSING_ROOM);

    SecurityCameraController securityCameraController =
        (SecurityCameraController) getController(AppUi.SECURITY_CAMERA_ROOM);
    ReceiptController receiptController = (ReceiptController) getController(AppUi.RECEIPT_ROOM);
    LostAndFoundController lostAndFoundController =
        (LostAndFoundController) getController(AppUi.LOST_FOUND_ROOM);

    // update timer for all rooms
    roomController.updateTimer(timeString);
    jimmyController.updateTimer(timeString);
    grandmaController.updateTimer(timeString);
    businessmanController.updateTimer(timeString);
    guessingController.updateTimer(timeString);
    securityCameraController.updateTimer(timeString);
    receiptController.updateTimer(timeString);
    lostAndFoundController.updateTimer(timeString);
  }

  /**
   * check if all clues are found and all characters are chatted. enable guess button if all clues
   * are found and all characters are chatted.
   *
   * @return boolean - if gussing is now allowed
   */
  public static boolean getIfCanGuess() {
    // checks all requirements to move on to guessing room
    boolean clueFound =
        LostAndFoundController.foundCufflink
            || ReceiptController.receiptInfoFound
            || SecurityCameraController.foundTimeOfTheft;
    boolean allChatted =
        BusinessmanController.businessmanChatted == true
            && GrandmaController.grandmaChatted == true
            && JimmyController.jimmyChatted == true;
    if (allChatted && clueFound) {
      return true;
    }
    return false;
  }

  /**
   * Handles the switch button click event to Jimmys's scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  public static void changeToJimmyScene(ActionEvent event) throws IOException {
    JimmyController jimmyController = (JimmyController) getController(AppUi.JIMMY_ROOM);
    switchRoot(AppUi.JIMMY_ROOM);
    jimmyController.startChat();
  }

  /**
   * Handles the switch button click event to grandma's scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  public static void changeToGrandmaScene(ActionEvent event) throws IOException {
    GrandmaController grandmaController = (GrandmaController) getController(AppUi.GRANDMA_ROOM);
    switchRoot(AppUi.GRANDMA_ROOM);
    grandmaController.startChat();
  }

  /**
   * Handles the switch button click event to grandma's scene.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  public static void changeToBusinessmanScene(ActionEvent event) throws IOException {
    BusinessmanController businessmanController =
        (BusinessmanController) getController(AppUi.BUSINESSMAN_ROOM);
    switchRoot(AppUi.BUSINESSMAN_ROOM);
    businessmanController.startChat();
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  public static void changeToGuessScene(ActionEvent event) throws IOException {
    GuessingController guessingController = (GuessingController) getController(AppUi.GUESSING_ROOM);
    switchRoot(AppUi.GUESSING_ROOM);
    guessingController.startChat();
    RoomController.context.handleGuessClick();
  }
}
