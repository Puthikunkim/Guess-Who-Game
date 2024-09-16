package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.controllers.BusinessmanController;
import nz.ac.auckland.se206.controllers.Controller;
import nz.ac.auckland.se206.controllers.GrandmaController;
import nz.ac.auckland.se206.controllers.JimmyController;
import nz.ac.auckland.se206.controllers.RoomController;

public class SceneManager {

  // Enum corresponding to each room
  public enum AppUi {
    MAIN_MENU,
    HOW_TO_PLAY,
    MAIN_ROOM,
    CHEST_ROOM,
    JIMMY_ROOM,
    GRANDMA_ROOM,
    BUSINESSMAN_ROOM,
    GUESSING_ROOM,
    SECURITY_CAMERA_ROOM,
    LOST_FOUND_ROOM,
    RECEIPT_ROOM,
  }

  private static Scene scene;
  private static Stage stage;
  // Maps containing the room and controller corresponding to the AppUI enum
  private static Map<AppUi, Parent> sceneMap = new HashMap<>();
  private static Map<AppUi, Controller> controllerMap = new HashMap<>();

  /**
   * Runs on application start(called in App.start), to initialaze all rooms and their controllers.
   *
   * @param stage
   * @throws IOException
   */
  public static void start(final Stage stage) throws IOException {
    // Save refrence to stage for easy resizing later
    SceneManager.stage = stage;
    // Load All Rooms for easy switching
    SceneManager.addUi(AppUi.MAIN_MENU, "mainMenu");
    SceneManager.addUi(AppUi.HOW_TO_PLAY, "howToPlay");
    SceneManager.addUi(AppUi.MAIN_ROOM, "room");
    SceneManager.addUi(AppUi.CHEST_ROOM, "chestInside");

    // chat-room
    SceneManager.addUi(AppUi.JIMMY_ROOM, "jimmy");
    SceneManager.addUi(AppUi.GRANDMA_ROOM, "grandma");
    SceneManager.addUi(AppUi.BUSINESSMAN_ROOM, "businessman");

    // guessing-room
    SceneManager.addUi(AppUi.GUESSING_ROOM, "guessing");
    SceneManager.addUi(AppUi.SECURITY_CAMERA_ROOM, "securityCamera");
    SceneManager.addUi(AppUi.LOST_FOUND_ROOM, "lostAndFound");
    SceneManager.addUi(AppUi.RECEIPT_ROOM, "receipt");

    // Set initial room to MainMenu.fxml
    Parent mainRoom = SceneManager.getRoot(AppUi.RECEIPT_ROOM);
    scene = new Scene(mainRoom);
    stage.setScene(scene);
    stage.show();
    mainRoom.requestFocus();
  }

  /**
   * Loads chosen FXML and saves its root and controller to the chosen AppUi.
   *
   * @param appUi
   * @param fxml
   * @throws IOException
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
   * @param appUi
   * @return
   */
  public static Parent getRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }

  /**
   * gets controller for the given AppUi.
   *
   * @param appUi
   * @return
   */
  public static Controller getController(AppUi appUi) {
    return controllerMap.get(appUi);
  }

  /**
   * Switch scene root to target AppUi scene.
   *
   * @param targetScene
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

  // timer
  public static void checkTimer(String timeString) {
    RoomController roomController = (RoomController) getController(AppUi.MAIN_ROOM);
    JimmyController jimmyController = (JimmyController) getController(AppUi.JIMMY_ROOM);
    GrandmaController grandmaController = (GrandmaController) getController(AppUi.GRANDMA_ROOM);
    BusinessmanController businessmanController =
        (BusinessmanController) getController(AppUi.BUSINESSMAN_ROOM);
    roomController.updateTimer(timeString);
    jimmyController.updateTimer(timeString);
    grandmaController.updateTimer(timeString);
    businessmanController.updateTimer(timeString);
  }
}
