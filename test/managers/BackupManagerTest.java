/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package managers;

import interfaces.IInventoryManager;
import java.util.List;
import models.Product;
import models.ToyProduct;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lifeo
 */
public class BackupManagerTest {

    private LogManager logManager;
    private BackupManager backupManager;
    private IInventoryManager inventoryManager;
    private static InventoryTableSeeder seeder;

    public BackupManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("derby.system.home", "database");
        boolean connected = DatabaseManager.getInstance().establishConnection();
        if (!connected) {
            fail("Could not establish DB connection");
        }

        seeder = new InventoryTableSeeder();
        seeder.initialiseAllTables();
    }

    @AfterClass
    public static void tearDownClass() {
        seeder.closeConnection();
    }

    @Before
    public void setUp() {
        seeder.resetProductsTable();
        seeder.resetLogsTable();
        seeder.resetBackupTable();

        this.logManager = new LogManager();
        this.backupManager = new BackupManager(logManager);
        this.inventoryManager = new InventoryManager();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of backupInventory method, of class BackupManager.
     */
    @Test
    public void testBackupInventory() {
        inventoryManager.addProduct("Toy", "Test Toy", 10, 15.99);
        backupManager.backupInventory();

        List<Product> backedUpProducts = backupManager.getBackup();
        assertFalse("Backup should not be empty", backedUpProducts.isEmpty());

        boolean found = backedUpProducts.stream().anyMatch(p
                -> p.getName().equals("Test Toy") && p.getPrice() == 15.99);
        assertTrue("Expected product not found in backup", found);
    }

    /**
     * Test of getBackup method, of class BackupManager.
     */
    @Test
    public void testGetBackup() {
        inventoryManager.addProduct("Toy", "Test Toy", 10, 15.99);
        backupManager.backupInventory();

        List<Product> backedUpProducts = backupManager.getBackup();
        assertFalse("Expected non-empty backup", backedUpProducts.isEmpty());
    }

    @Test
    public void testGetBackupWhenEmpty() {
        List<Product> backedUpProducts = backupManager.getBackup();
        assertTrue("Expected empty backup", backedUpProducts.isEmpty());
    }
}
