/**
 * Entry point for the Financial Manager application.
 * Launches the JavaFX application and sets up the initial scene with the login page.
 */
package com.example.financialmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FinancialManager extends Application {

  /**
   * Starts the JavaFX application.
   *
   * @param primaryStage The primary stage for the application
   * @throws Exception If an error occurs during the initialization of the primary stage
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("login-page.fxml"));
    Parent root = loader.load();
    primaryStage.setTitle("Financial Manager");
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  /**
   * The main method, launches the JavaFX application.
   *
   * @param args Command-line arguments (not used)
   */
  public static void main(String[] args) {
    launch(args);
  }
}
