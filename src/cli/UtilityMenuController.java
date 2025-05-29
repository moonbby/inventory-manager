/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cli;

import interfaces.IFileManager;
import interfaces.IInputHelper;
import interfaces.IInventoryManager;
import java.util.ArrayList;
import managers.BackupManager;
import managers.LogManager;

/**
 * Handles auxiliary inventory management features.
 *
 * Uses InputHelper for validation and delegates I/O tasks to LogManager,
 * BackupManager, and FileManager.
 */
public class UtilityMenuController {

    private final IInventoryManager inventoryManager;
    private final IFileManager fileManager;
    private final IInputHelper inputHelper;

    public UtilityMenuController(IInventoryManager inventoryManager, IFileManager fileManager, IInputHelper inputHelper) {
        this.inventoryManager = inventoryManager;
        this.fileManager = fileManager;
        this.inputHelper = inputHelper;
    }

    /**
     * Displays the system's activity log to the user.
     *
     * Prints all log entries to the console or a message if none exist.
     */
    public void viewLogsMenu() {
        ArrayList<String> logs = LogManager.getLogs();
        LogManager.log("Viewed activity log.");

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
    public void viewBackup() {
        ArrayList<String> backup = BackupManager.getBackup();

        if (backup == null || backup.isEmpty()) {
            System.out.println("No backup found.");
            return;
        }

        System.out.println("\n=== Backup Copy of Inventory ===");
        for (String entry : backup) {
            System.out.println(entry);
        }
    }

    /**
     * Creates a backup of the current inventory.
     *
     * Copies data to a backup file and confirms completion.
     */
    public void createBackup() {
        BackupManager.backupInventory();
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
        fileManager.exportLowStock(inventoryManager.getProductMap(), threshold);
    }

}
