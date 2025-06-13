/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author lifeo
 */
public class InputValidator {
    
        public static boolean isValidProductName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isValidProductQuantity(String input) {
        try {
            return Integer.parseInt(input) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidProductPrice(String input) {
        try {
            return Double.parseDouble(input) > 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidRestockAmount(String input) {
        try {
            return Integer.parseInt(input) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidPurchaseAmount(String input, int availableStock) {
        try {
            int amount = Integer.parseInt(input);
            return amount > 0 && amount <= availableStock;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidLowStockThreshold(String input) {
        try {
            return Integer.parseInt(input) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
