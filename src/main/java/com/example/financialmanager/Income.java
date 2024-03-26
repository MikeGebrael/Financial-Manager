/**
 * Represents an income in the Financial Manager application.
 * An income object encapsulates details such as name, amount, source, date, description, and ID.
 */
package com.example.financialmanager;

import java.time.LocalDate;

public class Income {
  private String name;
  private double amount;
  private String source;
  private LocalDate date;
  private String description;
  private int id;

  /**
   * Constructs an Income object with the provided details.
   *
   * @param name        Name of the income
   * @param amount      Amount of the income
   * @param source      Source of the income
   * @param date        Date of the income
   * @param description Description of the income
   * @param id          Unique identifier of the income
   */
  public Income(String name, double amount, String source, LocalDate date, String description, int id) {
    this.name = name;
    this.amount = amount;
    this.source = source;
    this.date = date;
    this.description = description;
    this.id = id;
  }

  // Getters and setters for the Income fields

  /**
   * Returns the name of the income.
   *
   * @return The name of the income
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the income.
   *
   * @param name The name of the income to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the amount of the income.
   *
   * @return The amount of the income
   */
  public double getAmount() {
    return amount;
  }

  /**
   * Sets the amount of the income.
   *
   * @param amount The amount of the income to set
   */
  public void setAmount(double amount) {
    this.amount = amount;
  }

  /**
   * Returns the source of the income.
   *
   * @return The source of the income
   */
  public String getSource() {
    return source;
  }

  /**
   * Sets the source of the income.
   *
   * @param source The source of the income to set
   */
  public void setSource(String source) {
    this.source = source;
  }

  /**
   * Returns the date of the income.
   *
   * @return The date of the income
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Sets the date of the income.
   *
   * @param date The date of the income to set
   */
  public void setDate(LocalDate date) {
    this.date = date;
  }

  /**
   * Returns the description of the income.
   *
   * @return The description of the income
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of the income.
   *
   * @param description The description of the income to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the ID of the income.
   *
   * @return The ID of the income
   */
  public int getId() {
    return id;
  }
}
