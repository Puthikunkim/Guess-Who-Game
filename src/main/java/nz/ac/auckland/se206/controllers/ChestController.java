package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/**
 * Controller class for the room view. Handles user interactions within the room where the user can
 * chat with customers and guess their profession.
 */
public class ChestController extends Controller {

  @FXML private Rectangle rubbishRect1;
  @FXML private Rectangle rubbishRect2;
  @FXML private Rectangle rubbishRect3;
  @FXML private Rectangle rubbishRect4;
  @FXML private ImageView rubbishImage1;
  @FXML private ImageView rubbishImage2;
  @FXML private ImageView rubbishImage3;
  @FXML private ImageView rubbishImage4;
  @FXML private ImageView hintImage;
  private Map<Rectangle, ImageView> rubbishRectToImage;
  private GameStateContext context;

  /** Initializes the room view. Sets the hint image to the correct image for the thief. */
  @FXML
  public void initialize() {
    RoomController roomController = (RoomController) SceneManager.getController(AppUi.MAIN_ROOM);
    context = roomController.getContext();
    rubbishRectToImage = new HashMap<>();
    rubbishRectToImage.put(rubbishRect1, rubbishImage1);
    rubbishRectToImage.put(rubbishRect2, rubbishImage2);
    rubbishRectToImage.put(rubbishRect3, rubbishImage3);
    rubbishRectToImage.put(rubbishRect4, rubbishImage4);
    hintImage.setImage(context.getPersonToGuess().getChestImage());
  }

  /**
   * if Rectangle clicked is not clue then destroy it and corresponding image, if it is clue send
   * message to chat box and move back to main scene.
   *
   * @param event
   * @throws IOException
   */
  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    String rectangleId = clickedRectangle.getId();
    if (rectangleId.contains("rubbish")) {
      ImageView image = rubbishRectToImage.get(clickedRectangle);
      clickedRectangle.setDisable(true);
      clickedRectangle.setVisible(false);
      image.setVisible(false);
      return;
    }

    context.playSound("Chest" + context.getPersonToGuess().getName());
    context.setChestChecked(true);
    SceneManager.switchRoot(AppUi.MAIN_ROOM);
  }

  public void setContext(GameStateContext context) {
    this.context = context;
  }
}
