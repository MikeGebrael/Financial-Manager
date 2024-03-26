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

public class UpdateExpenseController {

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

  private Expense expense;

  // Predefined sources
  private final ObservableList<String> predefinedSources = FXCollections.observableArrayList(
          "Rent", "Utilities", "Groceries", "Entertainment", "Transportation");

  /**
   * Initializes the data for the update expense form.
   *
   * @param expense The expense object to be updated.
   */
  public void initData(Expense expense) {
    this.expense = expense;
    try {
      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/financialmanager", "root", "root");
      PreparedStatement statement = conn.prepareStatement("SELECT * FROM expense WHERE expense_name = ?");
      statement.setString(1, expense.getName());
      ResultSet rs = statement.executeQuery();

      if (rs.next()) {
        double amount = rs.getDouble("amount");
        String source = rs.getString("source");
        Date date = rs.getDate("date");
        String description = rs.getString("description");

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
   * Initializes the ComboBox with predefined sources and makes it editable.
   */
  @FXML
  private void initialize() {
    sourceComboBox.setItems(predefinedSources);
    sourceComboBox.setEditable(true);
  }

  /**
   * Updates the expense record in the database based on the entered data.
   */
  @FXML
  private void updateExpense() {
    try {
      String name = nameField.getText();
      double amount = Double.parseDouble(amountField.getText());
      String source = sourceComboBox.getValue();
      Date date = Date.valueOf(datePicker.getValue());
      String description = descriptionField.getText();

      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/financialmanager", "root", "root");
      String sql = "UPDATE Expense SET amount = ?, date = ?, source = ?, description = ?, expense_name = ? WHERE expense_id = ?";
      PreparedStatement statement = conn.prepareStatement(sql);
      statement.setDouble(1, amount);
      statement.setDate(2, date);
      statement.setString(3, source);
      statement.setString(4, description);
      statement.setString(5, name);
      statement.setInt(6, expense.getId());
      int rowsAffected = statement.executeUpdate();

      if (rowsAffected > 0) {
        showAlert(Alert.AlertType.INFORMATION, "Expense Updated", "Expense successfully updated.");
      } else {
        showAlert(Alert.AlertType.ERROR, "Update Failed", "No expense found with the specified ID.");
      }
      Stage stage = (Stage) nameField.getScene().getWindow();
      stage.close();
      statement.close();
      conn.close();
    } catch (NumberFormatException e) {
      showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for the expense amount.");
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
