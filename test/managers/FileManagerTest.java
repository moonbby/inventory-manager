/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import interfaces.IFileManager;
import interfaces.IInventoryManager;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Product;
import models.ToyProduct;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the FileManager class.
 *
 * Validates reading and writing functionality using a dedicated test inventory
 * file. Ensures tests do not interfere with actual production data.
 */
public class FileManagerTest {

    private IFileManager fileManager;
    private IInventoryManager inventoryManager;
    private static final String TEST_FILE_PATH = "./resources/test_inventory.txt";

    /**
     * Set up a fresh instance of FileManager and InventoryManager before each
     * test. Injects the test-specific file path to avoid affecting production
     * files.
     */
    @BeforeEach
    public void setup() {
        fileManager = new FileManager(TEST_FILE_PATH);
        inventoryManager = new InventoryManager();
    }

    /**
     * Cleans up after each test by clearing the contents of the test file. This
     * ensures a clean state between tests without deleting the file itself.
     */
    @AfterEach
    public void cleanUp() throws IOException {
        PrintWriter pw = new PrintWriter(TEST_FILE_PATH);
        pw.close();
    }

    /**
     * Verifies that writing products to a file correctly creates and stores the
     * expected content.
     */
    @Test
    public void testWriteProducts_createsFileWithCorrectContent() throws IOException {
        Product product = new ToyProduct("Ball", 10, 9.99);
        inventoryManager.addProduct(product);

        fileManager.writeProducts(inventoryManager.getProductMap());

        // This line was added with guidance from ChatGPT assistance
        List<String> lines = Files.readAllLines(Paths.get(TEST_FILE_PATH));

        String expectedLine = product.getProductType() + ", " + product.getID() + ", "
                + product.getName() + ", " + product.getQuantity() + ", " + product.getPrice();
        assertEquals(1, lines.size());
        assertEquals(expectedLine, lines.get(0));
    }

    /**
     * Verifies that reading products from a file correctly reconstructs Product
     * objects.
     */
    @Test
    public void testReadProducts_loadsProductsCorrectly() {
        try {
            // Writes a sample product entry to the test file for loading verification.
            PrintWriter pw = new PrintWriter(new FileOutputStream(TEST_FILE_PATH));
            pw.println("Toy, P003, Chess, 72, 34.66");
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManagerTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        fileManager.readProducts(inventoryManager);
        Product loaded = inventoryManager.getProduct("P003");

        assertEquals("P003", loaded.getID());
        assertEquals("Chess", loaded.getName());
        assertEquals(72, loaded.getQuantity());
        assertEquals(34.66, loaded.getPrice(), 0.001);
        assertEquals("Toy", loaded.getProductType());
    }
}
