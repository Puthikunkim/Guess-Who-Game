package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.prompts.PromptEngineering;

/** Parent class for all rooms that have a character to chat with. */
public class ChatRoomController extends GameRoomController {

  /** The different characters, can change this to classes later down the line if needed */
  public enum Characters {
    JIMMY,
    BUSINESSMAN,
    GRANDMA
  }

  protected Characters character;
  private ChatCompletionRequest chatCompletionRequest; // Chat completion requests for each suspect
  private boolean chatStarted = false;
  private boolean chatted = false;
  @FXML private Label lblResponse;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;

  @FXML private Button btnCrimeScene;
  @FXML private Button btnJimmy;
  @FXML private Button btnGrandma;
  @FXML private Button btnBusinessman;

  private GameStateContext context = RoomController.getContext();

  private boolean canSwitch = true; // Boolean to track if the user can switch suspects

  /** when switched to clear the chat history. */
  @Override
  public void onSwitchTo() {
    super.onSwitchTo();
    txtaChat.clear();
  }

  /**
   * Initializes the room view. If it's the first time initialization, it will provide instructions
   * via text-to-speech.
   */
  @FXML
  public void initialize() {
    lblResponse.setVisible(false);
    startChat(); // Start the chat with the suspect to avoid lag when clicking on suspect for first
    // time
    // Add event handler for pressing Enter in txtInput
    txtInput.setOnKeyPressed(
        event -> {
          switch (event.getCode()) {
            case ENTER:
              try {
                onSendMessage(new ActionEvent()); // Trigger the send message method
              } catch (ApiProxyException | IOException e) {
                e.printStackTrace();
              }
              break;
            default:
              break;
          }
        });
  }

  /**
   * Generates the system prompt based on who the suspect is.
   *
   * @return the system prompt string
   */
  protected String getSystemPrompt(String prompt) {
    Map<String, String> map = new HashMap<>();
    map.put("profession", "not thief");
    return PromptEngineering.getPrompt(prompt, map);
  }

  /** Sets the suspect for the chat context and initializes the ChatCompletionRequest. */
  public void startChat() {
    // If the chat with the suspect has not started, initialize the ChatCompletionRequest and start
    if (!chatStarted) {
      // Initialize the ChatCompletionRequest
      try {
        ApiProxyConfig config = ApiProxyConfig.readConfig();
        chatCompletionRequest =
            new ChatCompletionRequest(config)
                .setN(1)
                .setTemperature(0.2)
                .setTopP(0.5)
                .setMaxTokens(100);
        String systemPrompt;
        // Set the system prompt based on the suspect
        if (character == Characters.JIMMY) {
          systemPrompt = getSystemPrompt("jimmy.txt");
        } else if (character == Characters.BUSINESSMAN) {
          systemPrompt = getSystemPrompt("businessman.txt");
        } else {
          systemPrompt = getSystemPrompt("grandma.txt");
        }
        // Start the chat with the suspect
        runGpt(new ChatMessage("system", systemPrompt));
        chatStarted = true;
      } catch (ApiProxyException e) {
        e.printStackTrace(); // Print the stack trace if there is an error
      }
    }
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  protected void appendChatMessage(ChatMessage msg) {

    // Append the user chat message to the relevant chat area for the suspect.
    if (msg.getRole().equals("user")) {
      txtaChat.appendText("You" + ": " + msg.getContent() + "\n\n");
    }

    // Append the assistant chat message to the relevant chat area for the suspect with the name of
    // the suspect.
    if (msg.getRole().equals("assistant")) {
      String role;
      if (character == Characters.JIMMY) {
        role = "Jimmy";
      } else if (character == Characters.BUSINESSMAN) {
        role = "Business Man";
      } else {
        role = "Grandma";
      }
      txtaChat.appendText(role + ": " + msg.getContent() + "\n\n");
    }
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  protected ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    lblResponse.setVisible(true);

    String role;
    if (character == Characters.JIMMY) {
      role = "Jimmy";
    } else if (character == Characters.BUSINESSMAN) {
      role = "Business Man";
    } else {
      role = "Grandma";
    }
    lblResponse.setText(role + " is responding...");
    canSwitch = false;
    enableButtons(canSwitch, SceneManager.getIfCanGuess());

    // Create a Task for the background thread to run the GPT model
    Task<Void> backgroundTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            chatCompletionRequest.addMessage(msg);
            try {
              ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
              Choice result = chatCompletionResult.getChoices().iterator().next();
              chatCompletionRequest.addMessage(result.getChatMessage());

              // Update the UI on the JavaFX Application Thread
              Platform.runLater(
                  () -> {
                    appendChatMessage(result.getChatMessage());
                    lblResponse.setVisible(false);
                    canSwitch = true;
                    enableButtons(canSwitch, SceneManager.getIfCanGuess());
                  });
            } catch (ApiProxyException e) {
              e.printStackTrace();
            }
            return null;
          }
        };

    // Create and start the background thread
    Thread backgroundThread = new Thread(backgroundTask);
    backgroundThread.setDaemon(true); // Ensure the thread does not prevent JVM shutdown
    backgroundThread.start();

    return null;
  }

  /**
   * Enables or disables the buttons in the chat room.
   *
   * @param enable whether to enable the buttons
   * @param canGuess whether the user can make a guess
   */
  protected void enableButtons(boolean enable, boolean canGuess) {
    // Enable or disable the buttons based on the enable parameter
    if (character != Characters.JIMMY) {
      btnJimmy.setDisable(!enable);
    }
    if (character != Characters.BUSINESSMAN) {
      btnBusinessman.setDisable(!enable);
    }
    if (character != Characters.GRANDMA) {
      btnGrandma.setDisable(!enable);
    }

    // Enable or disable the crime scene and send buttons based on the enable parameter
    btnCrimeScene.setDisable(!enable);
    btnSend.setDisable(!enable);
    if (canGuess == true) {
      btnGuess.setDisable(!enable);
    }
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  protected void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    context.playSound("button-4-214382");
    chatted = true;
    String message = txtInput.getText().trim();
    if (message.isEmpty() || !canSwitch) {
      return;
    }
    txtInput.clear();
    if (SceneManager.getIfCanGuess()) {
      btnGuess.setDisable(false);
    }
    // Create a ChatMessage object with the user's message, append it to the chat area and get a
    // response from the GPT model
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);
    runGpt(msg);
  }

  /**
   * THis method returns get chatted if the player has chatted with the character (Currently
   * Obsolete).
   *
   * @return chatted - if this character has been chatted with (currently obsolete)
   */
  public boolean getChatted() {
    return chatted;
  }
}
