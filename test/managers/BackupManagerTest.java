/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package managers;

import interfaces.IInventoryManager;
import java.util.List;
import models.Product;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the BackupManager class.
 *
 * Validates backup creation and retrieval logic using seeded database states.
 */
public class BackupManagerTest {

    private LogManager logManager;
    private BackupManager backupManager;
    private IInventoryManager inventoryManager;
    private static InventoryTableSeeder seeder;

    public BackupManagerTest() {
    }

    /**
     * Establishes database connection and initialises tables once before all tests.
     */
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

    /**
     * Closes database connection after all tests complete.
     */
    @AfterClass
    public static void tearDownClass() {
        seeder.closeConnection();
    }

    /**
     * Resets all tables before each test to maintain isolation and consistency.
     */
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
     * Verifies that calling backupInventory() correctly backs up inventory data.
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
     * Ensures getBackup() retrieves the backed-up data after a backup is created.
     */
    @Test
    public void testGetBackup() {
        inventoryManager.addProduct("Toy", "Test Toy", 10, 15.99);
        backupManager.backupInventory();

        List<Product> backedUpProducts = backupManager.getBackup();
        assertFalse("Expected non-empty backup", backedUpProducts.isEmpty());
    }

    /**
     * Ensures getBackup() returns an empty list when no backup has been created.
     */
    @Test
    public void testGetBackupWhenEmpty() {
        List<Product> backedUpProducts = backupManager.getBackup();
        assertTrue("Expected empty backup", backedUpProducts.isEmpty());
    }
}
