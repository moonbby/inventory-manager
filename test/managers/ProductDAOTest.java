/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package managers;

import java.util.List;
import models.Product;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for ProductDAO class.
 *
 * Validates CRUD operations and filtering of products using DAO pattern.
 */
public class ProductDAOTest {

    private ProductDAO productDAO;
    private static InventoryTableSeeder seeder;

    public ProductDAOTest() {
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

        this.productDAO = new ProductDAO();
    }

    @After
    public void tearDown() {
    }

    /**
     * Tests that a product can be added and retrieved successfully.
     */
    @Test
    public void testAddAndGetProduct() {
        Product p = productDAO.addProduct("Clothing", "DAO Jeans", 5, 55.5);
        assertNotNull(p);
        Product fetched = productDAO.getProduct(p.getID());
        assertEquals("DAO Jeans", fetched.getName());
    }

    /**
     * Expects an exception when trying to add a product with an invalid type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidProductType() {
        productDAO.addProduct("InvalidType", "Invalid", 1, 1.0);
    }

    /**
     * Tests that a product can be removed and is no longer retrievable afterwards.
     */
    @Test
    public void testRemoveProduct() {
        Product p = productDAO.addProduct("Clothing", "RemoveMe", 1, 1.1);
        boolean removed = productDAO.removeProduct(p.getID());
        assertTrue(removed);
        assertNull(productDAO.getProduct(p.getID()));
    }

    /**
     * Tests that a product's quantity can be updated correctly.
     */
    @Test
    public void testUpdateQuantity() {
        Product p = productDAO.addProduct("Toy", "DAO Toy", 10, 9.99);
        boolean updated = productDAO.updateQuantity(p.getID(), 3);
        assertTrue(updated);
        assertEquals(3, productDAO.getProduct(p.getID()).getQuantity());
    }

    /**
     * Tests that a valid product ID returns the correct product.
     */
    @Test
    public void testGetProduct_ValidId() {
        Product added = productDAO.addProduct("Clothing", "Test Jacket", 10, 50.0);
        Product fetched = productDAO.getProduct(added.getID());

        assertNotNull(fetched);
        assertEquals(added.getID(), fetched.getID());
        assertEquals("Test Jacket", fetched.getName());
    }

    /**
     * Tests that an invalid product ID returns null.
     */
    @Test
    public void testGetProduct_InvalidId() {
        Product fetched = productDAO.getProduct("NON_EXISTENT_ID");
        assertNull(fetched);
    }

    /**
     * Tests that getAllProducts returns an accurate list and grows after additions.
     */
    @Test
    public void testGetAllProducts() {
        int initialSize = productDAO.getAllProducts().size();

        productDAO.addProduct("Toy", "Rubik", 3, 12.0);
        productDAO.addProduct("Clothing", "Scarf", 1, 20.0);

        List<Product> products = productDAO.getAllProducts();

        assertNotNull(products);
        assertEquals(initialSize + 2, products.size());
    }

    /**
     * Tests that products below a given stock threshold are correctly identified.
     */
    @Test
    public void testGetLowStockProducts() {
        productDAO.addProduct("Toy", "LowToy", 3, 2.5);
        List<Product> low = productDAO.getLowStockProducts(5);
        assertFalse(low.isEmpty());
    }
}
