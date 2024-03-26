/**
 * Controller class for managing user login and registration in the Financial Manager application.
 * Provides functionality to validate user credentials, login, register, and display alert messages.
 */
package com.example.financialmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginPageController {

  @FXML
  private TextField loginUsernameField;

  @FXML
  private PasswordField loginPasswordField;

  @FXML
  private TextField registerUsernameField;

  @FXML
  private PasswordField registerPasswordField;

  @FXML
  private Button loginButton;

  @FXML
  private Button registerButton;

  // JDBC connection parameters
  private static final String JDBC_URL = "jdbc:mysql://localhost:3306/financialmanager";
  private static final String JDBC_USER = "root";
  private static final String JDBC_PASSWORD = "root";

  /**
   * Validates user credentials and performs login.
   */
  public void login() {
    String username = loginUsernameField.getText();
    String password = loginPasswordField.getText();

    // Check credentials against the database
    if (isValidUser(username, password)) {
      // Navigate to main menu or display a success message
      showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome " + username);
      PageSwitcher.switchScene("main-menu.fxml", loginButton);
    } else {
      // Display error message
      showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password");
    }
  }

  /**
   * Registers a new user.
   */
  public void register() {
    String username = registerUsernameField.getText();
    String password = registerPasswordField.getText();

    // Save user to the database
    if (saveUser(username, password)) {
      showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully");
      PageSwitcher.switchScene("main-menu.fxml", registerButton);
    } else {
      showAlert(Alert.AlertType.ERROR, "Registration Failed", "Failed to register user");
    }
  }

  /**
   * Checks if the provided username and password match any user in the database.
   *
   * @param username The username to validate
   * @param password The password to validate
   * @return true if the username and password are valid, otherwise false
   */
  private boolean isValidUser(String username, String password) {
    try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
         PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?")) {

      statement.setString(1, username);
      statement.setString(2, password);
      try (ResultSet resultSet = statement.executeQuery()) {
        return resultSet.next(); // Return true if a row is returned
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Saves a new user to the database.
   *
   * @param username The username of the new user
   * @param password The password of the new user
   * @return true if the user is successfully saved, otherwise false
   */
  private boolean saveUser(String username, String password) {
    try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
         PreparedStatement statement = connection.prepareStatement("INSERT INTO user (username, password) VALUES (?, ?)")) {

      statement.setString(1, username);
      statement.setString(2, password);
      int rowsInserted = statement.executeUpdate();
      return rowsInserted > 0; // Return true if at least one row is inserted
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Displays an alert dialog with the specified type, title, and content.
   *
   * @param type    The type of the alert (information, error, etc.)
   * @param title   The title of the alert
   * @param content The content of the alert
   */
  private void showAlert(Alert.AlertType type, String title, String content) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }
}
