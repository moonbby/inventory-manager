/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 * Utility class for validating user input in the inventory system.
 */
public class InputValidator {

    /**
     * Validates that the product name is not null or empty after trimming.
     *
     * @param name the product name to validate
     * @return true if valid; false otherwise
     */
    public static boolean isValidProductName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * Validates that the input string represents a non-negative integer.
     *
     * @param input the quantity input to validate
     * @return true if the input is a valid quantity; false otherwise
     */
    public static boolean isValidProductQuantity(String input) {
        try {
            return Integer.parseInt(input) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates that the input string represents a positive price.
     *
     * @param input the price input to validate
     * @return true if the input is a valid price; false otherwise
     */
    public static boolean isValidProductPrice(String input) {
        try {
            return Double.parseDouble(input) > 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates that the restock amount is a positive integer.
     *
     * @param input the restock input to validate
     * @return true if valid; false otherwise
     */
    public static boolean isValidRestockAmount(String input) {
        try {
            return Integer.parseInt(input) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates that the purchase amount is a positive integer and does not
     * exceed the available stock.
     *
     * @param input the purchase input to validate
     * @param availableStock the current available quantity in stock
     * @return true if the input is valid and within range; false otherwise
     */
    public static boolean isValidPurchaseAmount(String input, int availableStock) {
        try {
            int amount = Integer.parseInt(input);
            return amount > 0 && amount <= availableStock;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates that the low stock threshold is a positive integer.
     *
     * @param input the threshold input to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidLowStockThreshold(String input) {
        try {
            return Integer.parseInt(input) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
