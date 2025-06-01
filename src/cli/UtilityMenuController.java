/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cli;

import interfaces.IInputHelper;
import interfaces.IInventoryManager;
import java.util.List;
import managers.BackupManager;
import managers.LogManager;
import models.Product;
import utils.CLIPrinter;

/**
 * Handles auxiliary inventory management features.
 *
 * Uses InputHelper for validation and delegates I/O tasks to LogManager,
 * BackupManager, and FileManager.
 */
public class UtilityMenuController {

    private final IInventoryManager inventoryManager;
    private final IInputHelper inputHelper;
    private final LogManager logManager;
    private final BackupManager backupManager;

    public UtilityMenuController(IInventoryManager inventoryManager,
            IInputHelper inputHelper, LogManager logManager) {
        this.inventoryManager = inventoryManager;
        this.inputHelper = inputHelper;
        this.logManager = logManager;
        this.backupManager = new BackupManager(logManager);
    }

    /**
     * Displays the system's activity log to the user.
     *
     * Prints all log entries to the console or a message if none exist.
     */
    public void showLogsMenu() {
        List<String> logs = logManager.getLogs();

        if (logs == null || logs.isEmpty()) {
            System.out.println("No logs found.");
            return;
        }

        System.out.println("\n=== Activity Log ===");
        for (String entry : logs) {
            System.out.println(entry);
        }
    }

    /**
     * Shows the contents of the latest inventory backup file.
     *
     * Prints each entry or notifies if the file is missing or empty.
     */
    public void showBackup() {
        List<Product> backup = backupManager.getBackup();

        if (backup == null || backup.isEmpty()) {
            System.out.println("No backup found.");
            return;
        }

        System.out.println("\n=== Backup Copy of Inventory ===");
        CLIPrinter.printProducts(backup);
    }

    /**
     * Creates a backup of the current inventory.
     *
     * Copies data to a backup file and confirms completion.
     */
    public void createBackup() {
        backupManager.backupInventory();
        System.out.println("Backup created successfully.");
    }

    /**
     * Prompts for a stock threshold and exports low-stock products to file.
     *
     * Filters inventory below the threshold and delegates writing to
     * FileManager.
     */
    public void exportLowStockMenu() {
        int threshold = inputHelper.promptInt("Enter stock threshold to export (e.g., 5): ", 1);
        if (threshold == -1) {
            System.out.println("Cancelled.");
            return;
        }
        List<Product> lowStockProducts = inventoryManager.getLowStockProducts(threshold);
        CLIPrinter.printProducts(lowStockProducts);
    }

}
