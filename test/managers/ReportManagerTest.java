/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package managers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import models.Product;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for ReportManager class.
 *
 * Validates summary reporting, most expensive product retrieval, and low stock
 * filtering logic.
 */
public class ReportManagerTest {

    private ReportManager reportManager;
    private InventoryManager inventoryManager;
    private LogManager logManager;
    private static InventoryTableSeeder seeder;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    public ReportManagerTest() {
    }

    /**
     * Establishes database connection and initialises tables once before all
     * tests.
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

        this.inventoryManager = new InventoryManager();
        this.logManager = new LogManager();
        this.reportManager = new ReportManager(inventoryManager, logManager);

        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Tests that the summary report reflects added products accurately.
     * Validates category-wise and total product counts before and after
     * insertion.
     */
    @Test
    public void testShowSummaryReport() {
        List<String[]> summaryBefore = reportManager.getSummaryCounts();
        int toyCountBefore = getCountForType(summaryBefore, "Toy");
        int clothingCountBefore = getCountForType(summaryBefore, "Clothing");
        int totalBefore = getCountForType(summaryBefore, "Total");

        inventoryManager.addProduct("Toy", "Test Doll", 3, 10.0);
        inventoryManager.addProduct("Clothing", "Test Hat", 2, 5.0);

        List<String[]> summaryAfter = reportManager.getSummaryCounts();
        int toyCountAfter = getCountForType(summaryAfter, "Toy");
        int clothingCountAfter = getCountForType(summaryAfter, "Clothing");
        int totalAfter = getCountForType(summaryAfter, "Total");

        assertTrue("Toy count did not increase", toyCountAfter > toyCountBefore);
        assertTrue("Clothing count did not increase", clothingCountAfter > clothingCountBefore);
        assertTrue("Total count did not increase by 2", totalAfter == totalBefore + 2);
    }

    /**
     * Tests that the most expensive product is correctly identified.
     */
    @Test
    public void testShowMostExpensiveProduct() {
        inventoryManager.addProduct("Toy", "Test Doll", 5, 3.99);
        inventoryManager.addProduct("Clothing", "Test Coat", 2, 9999.99);

        Product expensive = reportManager.getMostExpensiveProduct();

        assertEquals("Test Coat", expensive.getName());
        assertEquals(9999.99, expensive.getPrice(), 0.01);
    }

    /**
     * Tests that exportLowStockMenu correctly filters and returns products
     * below a specified stock threshold.
     */
    @Test
    public void testExportLowStockMenu() {
        inventoryManager.addProduct("Toy", "Plush Bear", 2, 12.0);
        inventoryManager.addProduct("Clothing", "Winter Jacket", 15, 79.99);

        List<Product> lowStock = reportManager.exportLowStockMenu(5);

        assertNotNull(lowStock);
        assertFalse(lowStock.isEmpty());

        for (Product p : lowStock) {
            assertTrue(p.getQuantity() < 5);
        }

        boolean containsHighStock = lowStock.stream()
                .anyMatch(p -> p.getName().equals("Winter Jacket"));
        assertFalse("High-stock product should not appear in low stock list", containsHighStock);
    }

    /**
     * Helper method to extract category or total count from summary report.
     *
     * @param summary list of string arrays containing category and count
     * @param type category type ("Toy", "Clothing", or "Total")
     * @return integer count associated with the given type
     */
    private int getCountForType(List<String[]> summary, String type) {
        return summary.stream()
                .filter(row -> row[0].equalsIgnoreCase(type))
                .findFirst()
                .map(row -> Integer.parseInt(row[1]))
                .orElse(0);
    }
}
