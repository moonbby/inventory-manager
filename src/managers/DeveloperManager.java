/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

/**
 *
 * @author lifeo
 */
public class DeveloperManager {
    
    private final InventoryTableSeeder seeder = new InventoryTableSeeder();

    public void resetProductTable() {
        seeder.resetProductsTable();
    }

    public void resetLogTable() {
        seeder.resetLogsTable();
    }

    public void resetBackupTable() {
        seeder.resetBackupTable();
    }
}