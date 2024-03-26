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
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateIncomeController {

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

  private Income income;

  // Predefined sources
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
   * Initializes the ComboBox with predefined sources and makes it editable.
   */
  @FXML
  private void initialize() {
    sourceComboBox.setItems(predefinedSources);
    sourceComboBox.setEditable(true);
  }

  /**
   * Initializes the data for the update income form.
   *
   * @param income The income object to be updated.
   */
  public void initData(Income income) {
    this.income = income;
    try {
      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/financialmanager", "root", "root");
      PreparedStatement statement = conn.prepareStatement("SELECT * FROM Income WHERE income_name = ?");
      statement.setString(1, income.getName());
      ResultSet rs = statement.executeQuery();

      if (rs.next()) {
        String name = rs.getString("income_name");
        double amount = rs.getDouble("amount");
        String source = rs.getString("source");
        Date date = rs.getDate("date");
        String description = rs.getString("description");

        nameField.setText(name);
        amountField.setText(String.valueOf(amount));
        sourceComboBox.setValue(source);
        datePicker.setValue(date.toLocalDate());
        descriptionField.setText(description);
      }

      rs.close();
      statement.close();
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Updates the income record in the database based on the entered data.
   */
  @FXML
  private void updateIncome() {
    try {
      String name = nameField.getText();
      double amount = Double.parseDouble(amountField.getText());
      String source = sourceComboBox.getValue();
      Date date = Date.valueOf(datePicker.getValue());
      String description = descriptionField.getText();

      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/financialmanager", "root", "root");
      String sql = "UPDATE Income SET amount = ?, date = ?, source = ?, description = ?, income_name = ? WHERE income_id = ?";
      PreparedStatement statement = conn.prepareStatement(sql);
      statement.setDouble(1, amount);
      statement.setDate(2, date);
      statement.setString(3, source);
      statement.setString(4, description);
      statement.setString(5, name);
      statement.setInt(6, income.getId());
      int rowsAffected = statement.executeUpdate();

      if (rowsAffected > 0) {
        showAlert(Alert.AlertType.INFORMATION, "Income Updated", "Income successfully updated.");
      } else {
        showAlert(Alert.AlertType.ERROR, "Update Failed", "No income found with the specified ID.");
      }
      Stage stage = (Stage) nameField.getScene().getWindow();
      stage.close();
      statement.close();
      conn.close();
    } catch (NumberFormatException e) {
      showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for the income amount.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Displays an alert dialog with the specified type, title, and message.
   *
   * @param type    The type of alert dialog (e.g., INFORMATION, ERROR).
   * @param title   The title of the alert dialog.
   * @param message The message to be displayed in the alert dialog.
   */
  private void showAlert(Alert.AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.show();
  }
}
