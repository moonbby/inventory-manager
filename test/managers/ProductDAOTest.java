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
 *
 * @author lifeo
 */
public class ProductDAOTest {

    private ProductDAO productDAO;
    private static InventoryTableSeeder seeder;

    public ProductDAOTest() {
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

        this.productDAO = new ProductDAO();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addProduct method, of class ProductDAO.
     */
    @Test
    public void testAddAndGetProduct() {
        Product p = productDAO.addProduct("Clothing", "DAO Jeans", 5, 55.5);
        assertNotNull(p);
        Product fetched = productDAO.getProduct(p.getID());
        assertEquals("DAO Jeans", fetched.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidProductType() {
        productDAO.addProduct("InvalidType", "Invalid", 1, 1.0);
    }

    /**
     * Test of removeProduct method, of class ProductDAO.
     */
    @Test
    public void testRemoveProduct() {
        Product p = productDAO.addProduct("Clothing", "RemoveMe", 1, 1.1);
        boolean removed = productDAO.removeProduct(p.getID());
        assertTrue(removed);
        assertNull(productDAO.getProduct(p.getID()));
    }

    /**
     * Test of updateQuantity method, of class ProductDAO.
     */
    @Test
    public void testUpdateQuantity() {
        Product p = productDAO.addProduct("Toy", "DAO Toy", 10, 9.99);
        boolean updated = productDAO.updateQuantity(p.getID(), 3);
        assertTrue(updated);
        assertEquals(3, productDAO.getProduct(p.getID()).getQuantity());
    }

    /**
     * Test of getProduct method, of class ProductDAO.
     */
    @Test
    public void testGetProduct_ValidId() {
        Product added = productDAO.addProduct("Clothing", "Test Jacket", 10, 50.0);
        Product fetched = productDAO.getProduct(added.getID());

        assertNotNull(fetched);
        assertEquals(added.getID(), fetched.getID());
        assertEquals("Test Jacket", fetched.getName());
    }

    @Test
    public void testGetProduct_InvalidId() {
        Product fetched = productDAO.getProduct("NON_EXISTENT_ID");
        assertNull(fetched);
    }

    /**
     * Test of getAllProducts method, of class ProductDAO.
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
     * Test of getLowStockProducts method, of class ProductDAO.
     */
    @Test
    public void testGetLowStockProducts() {
        productDAO.addProduct("Toy", "LowToy", 3, 2.5);
        List<Product> low = productDAO.getLowStockProducts(5);
        assertFalse(low.isEmpty());
    }

}
