/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import models.Product;

/**
 * Interface defining operations for managing in-memory inventory.
 * 
 * Supports core CRUD operations, stock adjustments, and access to product data.
 * Enables flexibility for mocking or alternative inventory implementations.
 */
public interface IInventoryManager {

    /**
     * Adds a product to the inventory.
     * 
     * @param product the product to add
     */
    Product addProduct(String type, String name, int quantity, double price);

    /**
     * Removes a product by its ID.
     * 
     * @param id the product ID
     */
    boolean removeProduct(String id);

    /**
     * Decreases the quantity of a given product.
     * 
     * @param id the product ID
     * @param quantity the quantity to reduce
     */
    boolean reduceQuantity(String id, int quantity);

    /**
     * Increases the quantity of a given product.
     * 
     * @param id the product ID
     * @param quantity the quantity to add
     */
    boolean addQuantity(String id, int quantity);

    /**
     * Retrieves a product by its ID.
     * 
     * @param id the product ID
     * @return the product
     */
    Product getProduct(String id);

    /**
     * Retrieves all products in the inventory.
     * 
     * @return a collection of all products
     */
    List<Product> getAllProducts();

    /**
     * Checks if a product exists by its ID.
     * 
     * @param id the product ID
     * @return true if found, false otherwise
     */
    boolean hasProduct(String id);
    
    List<Product> getLowStockProducts(int threshold);
}
