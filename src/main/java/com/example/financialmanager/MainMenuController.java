/**
 * Controller class for managing the main menu of the Financial Manager application.
 * Provides functionality to display income and expense tables, navigate to income and expense pages,
 * calculate totals, and create pie charts for income and expense data.
 */
package com.example.financialmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainMenuController {

  @FXML
  private TableView<Income> incomeTable;

  @FXML
  private TableView<Expense> expenseTable;

  @FXML
  private Button expenseButton;

  @FXML
  private Button incomeButton;

  @FXML
  private TextField totalIncomeField;

  @FXML
  private TextField totalExpenseField;

  @FXML
  private TextField balanceField;

  @FXML
  private PieChart incomePieChart;

  @FXML
  private PieChart expensePieChart;

  // ObservableLists to hold income and expense data
  private ObservableList<Income> incomeList = FXCollections.observableArrayList();
  private ObservableList<Expense> expenseList = FXCollections.observableArrayList();

  /**
   * Initializes the main menu by setting up income and expense tables,
   * calculating totals, and creating pie charts.
   */
  @FXML
  private void initialize() {
    setupIncomeTable();
    setupExpenseTable();
    calculateTotals();
    createPieCharts();
  }

  /**
   * Sets up the income table by retrieving data from the database and populating the table.
   */
  private void setupIncomeTable() {
    try {
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/financialmanager", "root", "root");
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM Income");

      while (resultSet.next()) {
        Income income = new Income(
                resultSet.getString("income_name"),
                resultSet.getDouble("amount"),
                resultSet.getString("source"),
                resultSet.getDate("date").toLocalDate(),
                resultSet.getString("description"),
                resultSet.getInt("income_id")
        );
        incomeList.add(income);
      }

      resultSet.close();
      statement.close();
      connection.close();

      incomeTable.setItems(incomeList);
      setupIncomeTableColumns();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets up the expense table by retrieving data from the database and populating the table.
   */
  private void setupExpenseTable() {
    try {
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/financialmanager", "root", "root");
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM expense");

      while (resultSet.next()) {
        Expense expense = new Expense(
                resultSet.getString("expense_name"),
                resultSet.getDouble("amount"),
                resultSet.getString("source"),
                resultSet.getDate("date").toLocalDate(),
                resultSet.getString("description"),
                resultSet.getInt("expense_id")
        );
        expenseList.add(expense);
      }

      resultSet.close();
      statement.close();
      connection.close();

      expenseTable.setItems(expenseList);
      setupExpenseTableColumns();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets up columns for the income table.
   */
  private void setupIncomeTableColumns() {
    TableColumn<Income, String> nameColumn = new TableColumn<>("Name");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

    TableColumn<Income, Double> amountColumn = new TableColumn<>("Amount");
    amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

    TableColumn<Income, String> sourceColumn = new TableColumn<>("Source");
    sourceColumn.setCellValueFactory(new PropertyValueFactory<>("source"));

    TableColumn<Income, String> descriptionColumn = new TableColumn<>("Description");
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

    incomeTable.getColumns().setAll(nameColumn, amountColumn, sourceColumn, descriptionColumn);
  }

  /**
   * Sets up columns for the expense table.
   */
  private void setupExpenseTableColumns() {
    TableColumn<Expense, String> nameColumn = new TableColumn<>("Name");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

    TableColumn<Expense, Double> amountColumn = new TableColumn<>("Amount");
    amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

    TableColumn<Expense, String> sourceColumn = new TableColumn<>("Source");
    sourceColumn.setCellValueFactory(new PropertyValueFactory<>("source"));

    TableColumn<Expense, String> descriptionColumn = new TableColumn<>("Description");
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

    expenseTable.getColumns().setAll(nameColumn, amountColumn, sourceColumn, descriptionColumn);
  }

  /**
   * Switches the scene to the expense page.
   */
  @FXML
  private void showExpensePage() {
    PageSwitcher.switchScene("expenses-page.fxml", expenseButton);
  }

  /**
   * Switches the scene to the income page.
   */
  @FXML
  private void showIncomePage() {
    PageSwitcher.switchScene("income-page.fxml", incomeButton);
  }

  /**
   * Calculates total income, total expense, and balance.
   */
  private void calculateTotals() {
    double totalIncome = 0.0;
    double totalExpense = 0.0;

    // Calculate total income
    for (Income income : incomeList) {
      totalIncome += income.getAmount();
    }

    // Calculate total expense
    for (Expense expense : expenseList) {
      totalExpense += expense.getAmount();
    }

    // Update UI with calculated values
    totalIncomeField.setText("Total Income: " + totalIncome);
    totalExpenseField.setText("Total Expense: " + totalExpense);
    balanceField.setText("Balance: " + (totalIncome - totalExpense));
  }

/**
 * Creates pie charts for income and expense data.
 */
private void createPieCharts() {
  ObservableList<PieChart.Data> incomeChartData = FXCollections.observableArrayList();
  ObservableList<PieChart.Data> expenseChartData = FXCollections.observableArrayList();

  // Populate income pie chart data
  for (Income income : incomeList) {
    incomeChartData.add(new PieChart.Data(income.getName(), income.getAmount()));
  }

  // Populate expense pie chart data
  for (Expense expense : expenseList) {
    expenseChartData.add(new PieChart.Data(expense.getName(), expense.getAmount()));
  }

  // Set data to income and expense pie charts
  incomePieChart.setData(incomeChartData);
  expensePieChart.setData(expenseChartData);
}
}
