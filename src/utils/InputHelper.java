/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import interfaces.IInputHelper;
import interfaces.IInventoryManager;
import java.util.Scanner;

/**
 * Concrete implementation of IInputHelper for handling validated CLI user
 * input.
 *
 * Handles string and numeric prompts with input validation, enforces minimum
 * value rules, and verifies product ID existence.
 *
 * All prompts support graceful cancellation via the "exit" keyword.
 */
public class InputHelper implements IInputHelper {

    private final Scanner scan = new Scanner(System.in);
    private final IInventoryManager inventoryManager;

    public InputHelper(IInventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    /**
     * Prompts the user for a string input. Returns null if cancelled by typing
     * "exit".
     *
     * @param message the message to display to the user
     * @return the entered string or null if cancelled
     */
    @Override
    public String promptString(String message) {
        System.out.println(message);
        String input = scan.nextLine();
        if (input.equalsIgnoreCase("exit")) {
            return null;
        }
        return input;
    }

    /**
     * Prompts for an integer input with minimum value validation. Returns -1 if
     * cancelled by typing "exit".
     *
     * @param message the prompt to show
     * @param minValue the minimum accepted integer
     * @return a valid integer or -1 if cancelled
     */
    @Override
    public int promptInt(String message, int minValue) {
        while (true) {
            try {
                System.out.println(message);
                String input = scan.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    return -1;
                }

                int value = Integer.parseInt(input);
                if (value < minValue) {
                    System.out.println("Input must be at least " + minValue);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    /**
     * Prompts for a double input with minimum value validation. Returns -1 if
     * cancelled by typing "exit".
     *
     * @param message the prompt to display
     * @param minValue the minimum accepted value
     * @return a valid double or -1 if cancelled
     */
    @Override
    public double promptDouble(String message, double minValue) {
        while (true) {
            try {
                System.out.println(message);
                String input = scan.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    return -1;
                }

                double value = Double.parseDouble(input);
                if (value < minValue) {
                    System.out.println("Input must be at least " + minValue);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    /**
     * Prompts for a product ID and ensures it exists in the inventory. Returns
     * null if cancelled by typing "exit".
     *
     * @param message the prompt to display
     * @return a valid product ID or null if cancelled
     */
    @Override
    public String promptValidProductID(String message) {
        while (true) {
            System.out.println(message);
            String id = scan.nextLine();
            if (id.equalsIgnoreCase("exit")) {
                return null;
            }
            if (!inventoryManager.hasProduct(id)) {
                System.out.println("The product with that ID does not exist! Try again.");
            } else {
                return id;
            }
        }
    }
}
