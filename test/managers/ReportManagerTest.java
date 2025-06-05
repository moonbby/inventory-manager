/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package managers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
public class ReportManagerTest {

    private ReportManager reportManager;
    private InventoryManager inventoryManager;
    private static InventoryTableSeeder seeder;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    public ReportManagerTest() {
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

        this.reportManager = new ReportManager();
        this.inventoryManager = new InventoryManager();

        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Test of showSummaryReport method, of class ReportManager.
     */
    @Test
    public void testShowSummaryReport() {
        inventoryManager.addProduct("Toy", "Doll", 3, 10.0);
        inventoryManager.addProduct("Clothing", "Hat", 2, 5.0);

        reportManager.showSummaryReport();
        String output = outContent.toString();

        assertTrue(output.contains("=== Inventory Summary Report ==="));
        assertTrue(output.contains("Toy products:"));
        assertTrue(output.contains("Clothing products:"));
        assertTrue(output.contains("Total products:"));
    }

    /**
     * Test of showMostExpensiveProduct method, of class ReportManager.
     */
    @Test
    public void testShowMostExpensiveProduct() {
        inventoryManager.addProduct("Toy", "Doll", 5, 3.99);
        inventoryManager.addProduct("Clothing", "Coat", 2, 9999.99);

        reportManager.showMostExpensiveProduct();
        String output = outContent.toString();

        assertTrue(output.contains("=== Most Expensive Product ==="));
        assertTrue(output.contains("Coat")); // most expensive
        assertTrue(output.contains("Price: $9999.99"));
    }
}
