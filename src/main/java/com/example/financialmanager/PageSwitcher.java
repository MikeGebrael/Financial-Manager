package com.example.financialmanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The PageSwitcher class provides a utility method to switch scenes in a JavaFX application.
 */
public class PageSwitcher {

  /**
   * Switches the scene to the one specified by the FXML file name.
   *
   * @param fxmlFileName The name of the FXML file for the scene to switch to.
   * @param button       The button that triggers the scene switch. Used to get the current stage.
   */
  public static void switchScene(String fxmlFileName, Button button) {
    try {
      // Create an FXMLLoader object with the specified FXML file
      FXMLLoader loader = new FXMLLoader(PageSwitcher.class.getResource(fxmlFileName));
      // Load the FXML file to create the scene's layout
      Parent page = loader.load();
      // Create a new Scene object with the loaded layout
      Scene scene = new Scene(page);
      // Get the current stage from the button's scene
      Stage stage = (Stage) button.getScene().getWindow();
      // Set the new scene to the retrieved stage
      stage.setScene(scene);
    } catch (IOException e) {
      // Print the stack trace if an IOException occurs during scene switching
      e.printStackTrace();
    }
  }
}
