/**
 * Controller class for managing incomes in the Financial Manager application.
 * Provides functionality to add, update, and delete incomes, as well as navigate to other pages.
 * Manages the TableView displaying the list of incomes.
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

public class IncomePageController {

  @FXML
  private Button overviewPageButton;

  @FXML
  private Button expensePageButton;

  @FXML
  private TableView<Income> incomeTableView;

  @FXML
  private TableColumn<Income, String> nameColumn;

  @FXML
  private TableColumn<Income, Double> amountColumn;

  @FXML
  private TableColumn<Income, String> sourceColumn;

  @FXML
  private TableColumn<Income, String> descriptionColumn;

  /**
   * Initializes the controller.
   * Sets up the TableView columns and populates the TableView with existing incomes.
   */
  @FXML
  private void initialize() {
    initializeTableView();
    refreshTableView();
  }

  /**
   * Opens a dialog to add a new income.
   * Refreshes the TableView after adding the income.
   */
  @FXML
  private void openAddIncomeDialog() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("add-income.fxml"));
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
   * Opens a dialog to update the selected income.
   * Refreshes the TableView after updating the income.
   */
  @FXML
  private void openUpdateIncomeDialog() {
    Income selectedItem = incomeTableView.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("update-income.fxml"));
        Parent root = loader.load();

        UpdateIncomeController controller = loader.getController();
        controller.initData(selectedItem);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Income");
        stage.setScene(new Scene(root));
        stage.showAndWait();

        refreshTableView();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      showAlert(Alert.AlertType.WARNING, "No Income Selected", "Please select an income to update.");
    }
  }

  /**
   * Deletes the selected income.
   * Refreshes the TableView after deleting the income.
   */
  @FXML
  private void deleteIncome() {
    Income selectedItem = incomeTableView.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      try {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/financialmanager", "root", "root");
        String sql = "DELETE FROM Income WHERE income_name = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, selectedItem.getName());
        statement.executeUpdate();

        showAlert(Alert.AlertType.INFORMATION, "Income Deleted", "Income successfully deleted.");

        statement.close();
        conn.close();

        refreshTableView();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      showAlert(Alert.AlertType.WARNING, "No Income Selected", "Please select an income to delete.");
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
   * Navigates to the expenses page.
   */
  @FXML
  private void showExpensesPage() {
    PageSwitcher.switchScene("expenses-page.fxml", expensePageButton);
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
   * Refreshes the TableView by fetching and displaying the latest incomes from the database.
   */
  private void refreshTableView() {
    try {
      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/financialmanager", "root", "root");
      Statement statement = conn.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM Income");

      ObservableList<Income> incomeList = FXCollections.observableArrayList();
      while (resultSet.next()) {
        Income income = new Income(resultSet.getString("income_name"), resultSet.getDouble("amount"),
                resultSet.getString("source"), resultSet.getDate("date").toLocalDate(),
                resultSet.getString("description"), resultSet.getInt("income_id"));
        incomeList.add(income);
      }

      incomeTableView.setItems(incomeList);

      resultSet.close();
      statement.close();
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

/**
 * Displays an alert dialog with the specified type, title, and message.
 * @param type    Type of the alert (information, warning, etc.)
 * @param title   Title of the alert dialog
 * @param message Message content of the alert dialog
 */
private void showAlert(Alert.AlertType type, String title, String message) {
  Alert alert = new Alert(type);
  alert.setTitle(title);
  alert.setHeaderText(null);
  alert.setContentText(message);
  alert.showAndWait();
}
}
