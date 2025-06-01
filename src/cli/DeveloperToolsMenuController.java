/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cli;

import managers.InventoryTableSeeder;

/**
 *
 * @author lifeo
 */
public class DeveloperToolsMenuController {
    
    private final InventoryTableSeeder seeder = new InventoryTableSeeder(); 

    public void performProductTableReset() {
        seeder.resetProductsTable();
    }

    public void performLogsTableReset() {
        seeder.resetLogsTable();
    }

    public void performBackupTableReset() {
        seeder.resetBackupTable();
    }
}
