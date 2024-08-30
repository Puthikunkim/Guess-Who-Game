package nz.ac.auckland.se206;

import javafx.scene.image.Image;
import nz.ac.auckland.apiproxy.tts.TextToSpeechRequest.Voice;

public class Person {
  private boolean thief;
  private String name;
  private String chestMessage;
  private Image chestImage;
  private Voice voice;

  public Person(String name, boolean thief, Voice voice, String chestMessage, Image chestImage) {
    this.name = name;
    this.thief = thief;
    this.chestMessage = chestMessage;
    this.chestImage = chestImage;
    this.voice = voice;
  }

  public boolean getIsThief() {
    return thief;
  }

  public String getName() {
    return name;
  }

  public String getChestMessage() {
    return chestMessage;
  }

  public Image getChestImage() {
    return chestImage;
  }

  public Voice getVoice() {
    return voice;
  }
}
