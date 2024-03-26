/**
 * Controller class for managing expenses in the Financial Manager application.
 * Provides functionality to add, update, and delete expenses, as well as navigate to other pages.
 * Manages the TableView displaying the list of expenses.
 */
package com.example.financialmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExpensePageController {

  @FXML
  private Button overviewPageButton;

  @FXML
  private Button incomePageButton;

  @FXML
  private TableView<Expense> expenseTableView;

  @FXML
  private TableColumn<Expense, String> nameColumn;

  @FXML
  private TableColumn<Expense, Double> amountColumn;

  @FXML
  private TableColumn<Expense, String> sourceColumn;

  @FXML
  private TableColumn<Expense, String> descriptionColumn;

  /**
   * Initializes the controller.
   * Sets up the TableView columns and populates the TableView with existing expenses.
   */
  @FXML
  private void initialize() {
    initializeTableView();
    refreshTableView();
  }

  /**
   * Opens a dialog to add a new expense.
   * Refreshes the TableView after adding the expense.
   */
  @FXML
  private void openAddExpenseDialog() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("add-expense.fxml"));
      Parent root = loader.load();

      Stage stage = new Stage();
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setTitle("Add Income");
      stage.setScene(new Scene(root));
      stage.showAndWait();

      refreshTableView();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Opens a dialog to update the selected expense.
   * Refreshes the TableView after updating the expense.
   */
  @FXML
  private void openUpdateExpenseDialog() {
    Expense selectedItem = expenseTableView.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("update-expense.fxml"));
        Parent root = loader.load();

        UpdateExpenseController controller = loader.getController();
        controller.initData(selectedItem);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Expense");
        stage.setScene(new Scene(root));
        stage.showAndWait();

        refreshTableView();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      showAlert(Alert.AlertType.WARNING, "No Expense Selected", "Please select an Expense to update.");
    }
  }

  /**
   * Deletes the selected expense.
   * Refreshes the TableView after deleting the expense.
   */
  @FXML
  private void deleteExpense() {
    Expense selectedItem = expenseTableView.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/financialmanager", "root", "root");
        String sql = "DELETE FROM Expense WHERE expense_name = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, selectedItem.getName());
        statement.executeUpdate();

        showAlert(Alert.AlertType.INFORMATION, "Expense Deleted", "Expense successfully deleted.");

        statement.close();
        conn.close();

        refreshTableView();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      showAlert(Alert.AlertType.WARNING, "No Expense Selected", "Please select an Expense to delete.");
    }
  }

  /**
   * Navigates to the overview page.
   */
  @FXML
  private void showOverviewPage() {
    PageSwitcher.switchScene("main-menu.fxml", overviewPageButton);
  }

  /**
   * Navigates to the income page.
   */
  @FXML
  private void showIncomePage() {
    PageSwitcher.switchScene("income-page.fxml", incomePageButton);
  }

  /**
   * Initializes the TableView by setting up column cell value factories.
   */
  private void initializeTableView() {
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
    sourceColumn.setCellValueFactory(new PropertyValueFactory<>("source"));
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
  }

  /**
   * Refreshes the TableView by fetching and displaying the latest expenses from the database.
   */
  private void refreshTableView() {
    try {
      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/financialmanager", "root", "root");
      Statement statement = conn.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM Expense");

      ObservableList<Expense> expenseList = FXCollections.observableArrayList();
      while (resultSet.next()) {
        Expense expense = new Expense(resultSet.getString("expense_name"), resultSet.getDouble("amount"),
                resultSet.getString("source"), resultSet.getDate("date").toLocalDate(),
                resultSet.getString("description"), resultSet.getInt("expense_id"));
        expenseList.add(expense);
      }

      expenseTableView.setItems(expenseList);

      resultSet.close();
      statement.close();
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Displays an alert dialog with the specified type, title, and message.
   *
   * @param type    Type of the alert (information, warning, etc.)
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
