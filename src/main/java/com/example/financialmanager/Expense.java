/**
 * Represents an expense in the Financial Manager application.
 * An expense object encapsulates details such as name, amount, source, date, description, and ID.
 */
package com.example.financialmanager;

import java.time.LocalDate;

public class Expense {
  private String name;
  private double amount;
  private String source;
  private LocalDate date;
  private String description;
  private final int id;

  /**
   * Constructs an Expense object with the provided details.
   *
   * @param name        Name of the expense
   * @param amount      Amount of the expense
   * @param source      Source of the expense
   * @param date        Date of the expense
   * @param description Description of the expense
   * @param id          Unique identifier of the expense
   */
  public Expense(String name, double amount, String source, LocalDate date, String description, int id) {
    this.name = name;
    this.amount = amount;
    this.source = source;
    this.date = date;
    this.description = description;
    this.id = id;
  }

  // Getters and setters for the Expense fields

  /**
   * Returns the name of the expense.
   *
   * @return The name of the expense
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the expense.
   *
   * @param name The name of the expense to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the amount of the expense.
   *
   * @return The amount of the expense
   */
  public double getAmount() {
    return amount;
  }

  /**
   * Sets the amount of the expense.
   *
   * @param amount The amount of the expense to set
   */
  public void setAmount(double amount) {
    this.amount = amount;
  }

  /**
   * Returns the source of the expense.
   *
   * @return The source of the expense
   */
  public String getSource() {
    return source;
  }

  /**
   * Sets the source of the expense.
   *
   * @param source The source of the expense to set
   */
  public void setSource(String source) {
    this.source = source;
  }

  /**
   * Returns the date of the expense.
   *
   * @return The date of the expense
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Sets the date of the expense.
   *
   * @param date The date of the expense to set
   */
  public void setDate(LocalDate date) {
    this.date = date;
  }

  /**
   * Returns the description of the expense.
   *
   * @return The description of the expense
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of the expense.
   *
   * @param description The description of the expense to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the ID of the expense.
   *
   * @return The ID of the expense
   */
  public int getId() {
    return id;
  }
}
