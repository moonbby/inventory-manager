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
 *
 * @author lifeo
 */
public class InventoryManagerTest {

    private IInventoryManager inventoryManager;
    private static InventoryTableSeeder seeder;

    public InventoryManagerTest() {
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

        this.inventoryManager = new InventoryManager();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addProduct method, of class InventoryManager.
     */
    @Test
    public void testAddProduct() {
        Product product = inventoryManager.addProduct("Clothing", "Test Shirt", 10, 29.99);
        assertNotNull(product);
        assertEquals("Test Shirt", product.getName());
        assertEquals("Clothing", product.getProductType());
    }

    @Test
    public void testAddProduct_InvalidType() {
        Product result = inventoryManager.addProduct("Food", "Apple", 10, 5.0);
        assertNull(result);
    }

    @Test
    public void testAddProduct_NegativeQuantity() {
        Product result = inventoryManager.addProduct("Toy", "Negative", -5, 5.0);
        assertNull(result);
    }

    @Test
    public void testAddProduct_NegativePrice() {
        Product result = inventoryManager.addProduct("Toy", "Freebie", 1, -3.0);
        assertNull(result);
    }

    @Test
    public void testAddProduct_NullName() {
        Product result = inventoryManager.addProduct("Clothing", null, 1, 10.0);
        assertNull(result);
    }

    @Test
    public void testAddProduct_EmptyName() {
        Product result = inventoryManager.addProduct("Toy", "", 1, 10.0);
        assertNull(result);
    }

    /**
     * Test of removeProduct method, of class InventoryManager.
     */
    @Test
    public void testRemoveProduct() {
        Product product = inventoryManager.addProduct("Toy", "Test Remove", 3, 9.99);
        boolean removed = inventoryManager.removeProduct(product.getID());
        assertTrue(removed);
        assertNull(inventoryManager.getProduct(product.getID()));
    }

    @Test
    public void testRemoveProduct_InvalidID() {
        assertFalse(inventoryManager.removeProduct("NO_SUCH_ID"));
    }

    /**
     * Test of reduceQuantity method, of class InventoryManager.
     */
    @Test
    public void testReduceQuantity() {
        Product product = inventoryManager.addProduct("Clothing", "Test Coat", 10, 59.99);
        boolean updated = inventoryManager.reduceQuantity(product.getID(), 5);
        assertTrue(updated);
        assertEquals(5, inventoryManager.getProduct(product.getID()).getQuantity());
    }

    @Test
    public void testReduceQuantity_NegativeQuantity() {
        Product product = inventoryManager.addProduct("Clothing", "Test", 10, 10.99);
        boolean updated = inventoryManager.reduceQuantity(product.getID(), -2);
        assertFalse(updated);
        assertEquals(10, inventoryManager.getProduct(product.getID()).getQuantity());
    }

    @Test
    public void testReduceQuantity_InvalidID() {
        boolean updated = inventoryManager.reduceQuantity("INVALID_ID", 5);
        assertFalse(updated);
    }

    @Test
    public void testReduceQuantity_BeyondStock() {
        Product product = inventoryManager.addProduct("Toy", "Undersell", 2, 5.99);
        boolean result = inventoryManager.reduceQuantity(product.getID(), 10);
        assertTrue(result);
        assertEquals(0, inventoryManager.getProduct(product.getID()).getQuantity());
    }

    /**
     * Test of addQuantity method, of class InventoryManager.
     */
    @Test
    public void testAddQuantity() {
        Product product = inventoryManager.addProduct("Clothing", "Test Coat", 10, 59.99);
        boolean updated = inventoryManager.addQuantity(product.getID(), 5);
        assertTrue(updated);
        assertEquals(15, inventoryManager.getProduct(product.getID()).getQuantity());
    }

    @Test
    public void testAddQuantity_NegativeQuantity() {
        Product product = inventoryManager.addProduct("Toy", "Test", 3, 9.99);
        boolean updated = inventoryManager.addQuantity(product.getID(), -2);
        assertFalse(updated);
        assertEquals(3, inventoryManager.getProduct(product.getID()).getQuantity());
    }

    @Test
    public void testAddQuantity_InvalidID() {
        assertFalse(inventoryManager.addQuantity("INVALID_ID", 3));
    }

    /**
     * Test of getProduct method, of class InventoryManager.
     */
    @Test
    public void testGetProduct() {
        Product added = inventoryManager.addProduct("Toy", "Test Toy", 5, 19.99);
        Product fetched = inventoryManager.getProduct(added.getID());
        assertNotNull(fetched);
        assertEquals("Test Toy", fetched.getName());
    }

    @Test
    public void testGetProduct_InvalidID() {
        assertNull(inventoryManager.getProduct("INVALID_ID"));
    }

    /**
     * Test of getAllProducts method, of class InventoryManager.
     */
    @Test
    public void testGetAllProducts() {
        int initialSize = inventoryManager.getAllProducts().size();
        inventoryManager.addProduct("Toy", "Extra 1", 1, 1.0);
        inventoryManager.addProduct("Clothing", "Extra 2", 2, 2.0);
        int newSize = inventoryManager.getAllProducts().size();
        assertEquals(initialSize + 2, newSize);
    }

    /**
     * Test of hasProduct method, of class InventoryManager.
     */
    @Test
    public void testHasProduct_True() {
        boolean result = inventoryManager.hasProduct("P001"); // seeded product
        assertTrue(result);
    }

    @Test
    public void testHasProduct_False() {
        boolean result = inventoryManager.hasProduct("NON_EXISTENT");
        assertFalse(result);
    }

    /**
     * Test of getLowStockProducts method, of class InventoryManager.
     */
    @Test
    public void testGetLowStockProducts() {
        List<Product> lowStock = inventoryManager.getLowStockProducts(10);

        assertNotNull(lowStock);
        assertFalse(lowStock.isEmpty());
        for (Product p : lowStock) {
            assertTrue(p.getQuantity() < 10);
        }
    }

}
