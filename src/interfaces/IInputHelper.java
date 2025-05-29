/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

/**
 * Interface for centralised user input handling in the CLI.
 *
 * Supports validated string, integer, and double inputs with cancellation
 * support. Also ensures that product IDs entered exist in the inventory.
 */
public interface IInputHelper {

    /**
     * Prompts the user for a string input.
     *
     * @param message the prompt to display
     * @return the user input, or null if cancelled
     */
    String promptString(String message);

    /**
     * Prompts the user for an integer input within a minimum value constraint.
     *
     * @param message the prompt to display
     * @param minValue the minimum acceptable value
     * @return the user input, or -1 if cancelled
     */
    int promptInt(String message, int minValue);

    /**
     * Prompts the user for a double input with minimum value validation.
     *
     * @param message the prompt to display
     * @param minValue the minimum acceptable value
     * @return the user input, or -1 if cancelled
     */
    double promptDouble(String message, double minValue);

    /**
     * Prompts the user for a valid product ID that exists in inventory.
     *
     * @param message the prompt to display
     * @return a valid product ID, or null if cancelled
     */
    String promptValidProductID(String message);
}
