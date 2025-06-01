/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cli;

import interfaces.IInputHelper;
import interfaces.IInventoryManager;
import java.util.InputMismatchException;
import java.util.Scanner;
import managers.BackupManager;
import managers.LogManager;
import managers.ReportManager;

/**
 * Controls the main navigation flow of the programme and manages menu
 * transitions. Routes user input to core modules and ensures a smooth user
 * experience through robust input validation and graceful exits.
 */
public class MainMenuController {

    private final Scanner scan = new Scanner(System.in);
    private final IInventoryManager inventoryManager;
    private final IInputHelper inputHelper;
    private final LogManager logManager;
    private final UtilityMenuController utilityMenuController;
    private final ProductMenuController productMenuController;
    private final ReportManager reportManager = new ReportManager();
    private final BackupManager backupManager = new BackupManager();


    public MainMenuController(IInventoryManager inventoryManager, IInputHelper inputHelper, LogManager logManager,
            UtilityMenuController utilityMenuController, ProductMenuController productMenuController) {
        this.inventoryManager = inventoryManager;
        this.inputHelper = inputHelper;
        this.logManager = logManager;
        this.utilityMenuController = utilityMenuController;
        this.productMenuController = productMenuController;
    }

    /**
     * Prints the main menu options to the console.
     *
     * Each option corresponds to a key system function.
     */
    public void printMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("Option 1: Add a new product");
        System.out.println("Option 2: Remove an existing product");
        System.out.println("Option 3: Restock a product");
        System.out.println("Option 4: Purchase a product");
        System.out.println("Option 5: View all products");
        System.out.println("Option 6: Utilities");
        System.out.println("Option 7: View reports");
        System.out.println("Option 8: Save and Exit\n");
    }

    /**
     * Handles user input for main menu navigation.
     *
     * Validates input type and value, then delegates to the appropriate feature
     * controller. Includes input mismatch handling to prevent program crashes.
     */
    public void handleUserInput() {
        try {
            int input = scan.nextInt();
            scan.nextLine();

            switch (input) {
                case 1 ->
                    productMenuController.addProductMenu();
                case 2 ->
                    productMenuController.removeProductMenu();
                case 3 ->
                    productMenuController.restockProductMenu();
                case 4 ->
                    productMenuController.purchaseProductMenu();
                case 5 ->
                    productMenuController.viewProductsMenu();
                case 6 ->
                    viewUtilitiesMenu();
                case 7 ->
                    viewReportMenu();
                case 8 ->
                    saveExitMenu();
                default ->
                    System.out.println("Invalid input! Please enter a number from 1 to 8. Try again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            scan.nextLine();
        }
    }

    /**
     * Displays and handles the Utilities Menu options.
     */
    public void viewUtilitiesMenu() {
        while (true) {
            System.out.println("\n=== Utilities Menu ===");
            System.out.println("1. View activity log");
            System.out.println("2. View backup file");
            System.out.println("3. Create inventory backup");
            System.out.println("4. Export low stock");
            System.out.println("5. Back to main menu");

            String input = inputHelper.promptString("");
            if (input == null || input.equals("5")) {
                return;
            }

            switch (input) {
                case "1" ->
                    utilityMenuController.viewLogsMenu();
                case "2" ->
                    utilityMenuController.viewBackup();
                case "3" ->
                    utilityMenuController.createBackup();
                case "4" ->
                    utilityMenuController.exportLowStockMenu();
                default ->
                    System.out.println("Invalid input! Please enter a number from 1 to 5. Try again.");
            }
        }
    }

    /**
     * Displays and handles the Report Menu options.
     */
    public void viewReportMenu() {
        while (true) {
            System.out.println("\n=== Report Menu ===");
            System.out.println("1. View summary report");
            System.out.println("2. View most expensive product");
            System.out.println("3. Back to main menu");

            String input = inputHelper.promptString("");
            if (input == null || input.equals("3")) {
                return;
            }

            switch (input) {
                case "1" ->
                    reportManager.viewSummaryReport();
                case "2" ->
                    reportManager.viewMostExpensiveProduct();
                default ->
                    System.out.println("Invalid input! Please enter a number from 1 to 3. Try again.");
            }
        }
    }

    /**
     * Saves the current inventory to file and creates a backup before exiting
     * the programme.
     *
     * Prompts the user for confirmation and allows cancellation. Ensures all
     * changes are persisted before safely terminating the application.
     */
    public void saveExitMenu() {
        while (true) {
            String confirm = inputHelper.promptString("Are you sure you want to save and exit? (yes/no):");
            if (confirm == null || confirm.equalsIgnoreCase("no")) {
                System.out.println("Cancelled.");
                return;
            } else if (confirm.equalsIgnoreCase("yes")) {
                backupManager.backupInventory();
                System.out.println("Inventory backup created successfully. Goodbye!");
                logManager.log("User chose to save and exit.");
                System.exit(0);
            } else {
                System.out.println("Invalid input! Input must be either yes or no. Try again.");
            }
        }
    }

}
