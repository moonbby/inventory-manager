/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import interfaces.IInventoryManager;
import models.ClothingProduct;
import models.Product;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the InventoryManager class.
 *
 * Verifies in-memory operations. Each test ensures the inventory state reflects
 * the intended outcome after specific actions.
 */
public class InventoryManagerTest {

    private IInventoryManager inventoryManager;

    /**
     * Runs before each test case to provide a fresh InventoryManager instance.
     * Ensures that test state does not carry over between tests.
     */
    @BeforeEach
    public void setup() {
        inventoryManager = new InventoryManager();
    }

    /**
     * Tests adding a product to the inventory. Verifies that the product can be
     * retrieved afterward using its ID.
     */
    @Test
    public void testAddProduct() {
        Product product = new ClothingProduct("T-Shirt", 10, 19.99);
        inventoryManager.addProduct(product);
        assertEquals(product, inventoryManager.getProduct(product.getID()));
    }

    /**
     * Tests removing a product from the inventory. Ensures the product is no
     * longer accessible after removal.
     */
    @Test
    public void testRemoveProduct() {
        Product product = new ClothingProduct("Jeans", 5, 49.99);
        inventoryManager.addProduct(product);
        inventoryManager.removeProduct(product.getID());
        assertNull(inventoryManager.getProduct(product.getID()));
    }

    /**
     * Tests increasing and decreasing product quantity. Verifies that the
     * quantity is adjusted correctly and updates reflect immediately.
     */
    @Test
    public void testAddAndReduceQuantity() {
        Product product = new ClothingProduct("Jacket", 10, 59.99);
        inventoryManager.addProduct(product);

        inventoryManager.addQuantity(product.getID(), 5);
        assertEquals(15, product.getQuantity());

        inventoryManager.reduceQuantity(product.getID(), 3);
        assertEquals(12, product.getQuantity());
    }

    /**
     * Tests reducing quantity below zero. Ensures that quantity does not go
     * negative and is set to zero instead.
     */
    @Test
    public void testReduceQuantityBelowZero() {
        Product product = new ClothingProduct("Hat", 2, 14.99);
        inventoryManager.addProduct(product);

        inventoryManager.reduceQuantity(product.getID(), 5);
        assertEquals(0, product.getQuantity());
    }

}
