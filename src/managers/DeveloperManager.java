/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

/**
 * Provides developer-facing operations for resetting key database tables
 * (Products, Logs, Backup).
 */
public class DeveloperManager {

    private final InventoryTableSeeder seeder = new InventoryTableSeeder();

    /**
     * Resets the products table to its default seeded state. Clears existing
     * entries and inserts predefined sample data.
     */
    public void resetProductTable() {
        seeder.resetProductsTable();
    }

    /**
     * Clears all entries from the logs table. Used to remove recorded
     * application activity.
     */
    public void resetLogTable() {
        seeder.resetLogsTable();
    }

    /**
     * Clears the backup table to remove previously stored backup records.
     */
    public void resetBackupTable() {
        seeder.resetBackupTable();
    }
}
