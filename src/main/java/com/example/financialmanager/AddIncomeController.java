/**
 * Controller class for adding income in the Financial Manager application.
 * Allows users to input income details such as name, amount, source, date, and description,
 * and adds them to the database upon submission.
 */
package com.example.financialmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddIncomeController {

  //FXML fields for accessing UI components
  @FXML
  private TextField nameField;

  @FXML
  private TextField amountField;

  @FXML
  private ComboBox<String> sourceComboBox;

  @FXML
  private DatePicker datePicker;

  @FXML
  private TextField descriptionField;

  //List of predefined income sources
  private final ObservableList<String> predefinedSources = FXCollections.observableArrayList(
          "Employment Income",
          "Self-Employment Income",
          "Investment Income",
          "Rental Income",
          "Royalty Income",
          "Dividend Income",
          "Capital Gains",
          "Interest Income",
          "Commission Income",
          "Online Income",
          "Passive Income",
          "Side Hustle Income",
          "Monetized Hobby Income",
          "Crowdfunding Income",
          "Annuity Income");

  /**
   * Initializes the controller.
   * Sets up the sourceComboBox with predefined income sources.
   */
  @FXML
  private void initialize() {
    sourceComboBox.setItems(predefinedSources);
    sourceComboBox.setEditable(true);
  }

  /**
   * Adds an income with the provided details to the database.
   * Called when the user clicks the "Add Income" button.
   * Shows an error message if the amount field contains invalid input.
   */
  @FXML
  private void addIncome() {
    try {
      String name = nameField.getText();
      double amount = Double.parseDouble(amountField.getText());
      String source = sourceComboBox.getValue();
      LocalDate date = datePicker.getValue();
      String description = descriptionField.getText();

      // Insert income data into database
      insertIncome(name, amount, source, date, description);

      showAlert(Alert.AlertType.INFORMATION, "Income Added", "Income successfully added.");

      // Close the add income window
      Stage stage = (Stage) nameField.getScene().getWindow();
      stage.close();
    } catch (NumberFormatException e) {
      showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for the income amount.");
    }
  }

  /**
   * Inserts a new income into the database with the provided details.
   *
   * @param name        Name of the income
   * @param amount      Amount of the income
   * @param source      Source of the income
   * @param date        Date of the income
   * @param description Description of the income
   */
  private void insertIncome(String name, double amount, String source, LocalDate date, String description) {
    try {
      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/financialmanager", "root", "root");
      String sql = "INSERT INTO Income (income_name, amount, source, date, description) VALUES (?, ?, ?, ?, ?)";
      PreparedStatement statement = conn.prepareStatement(sql);
      statement.setString(1, name);
      statement.setDouble(2, amount);
      statement.setString(3, source);
      statement.setDate(4, java.sql.Date.valueOf(date));
      statement.setString(5, description);
      statement.executeUpdate();
      statement.close();
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Displays an alert dialog with the specified type, title, and message.
   *
   * @param type    Type of the alert (information, error, etc.)
   * @param title   Title of the alert
   * @param message Message to be displayed in the alert
   */
  private void showAlert(Alert.AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
