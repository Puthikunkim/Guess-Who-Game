package nz.ac.auckland.se206.controllers;

/** parent class for controllers so that SceneManager can handle them cleanly */
public abstract class Controller {
  /** Method that can be called on children to have unique usage fro all rooms */
  public void onSwitchTo() {}
}
