/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import models.Product;

/**
 * Interface for managing inventory operations within the system.
 *
 * Provides core CRUD functionality, stock-level adjustments, and product retrieval methods.
 * Designed to support flexible implementations across GUI, database, or test contexts.
 */
public interface IInventoryManager {

    /**
     * Adds a new product to the inventory.
     *
     * @param type the product type (e.g., Clothing, Toy)
     * @param name the product name
     * @param quantity the initial quantity
     * @param price the price per unit
     * @return the newly added product; null if creation failed
     */
    Product addProduct(String type, String name, int quantity, double price);

    /**
     * Removes a product from the inventory by its ID.
     *
     * @param id the unique product ID
     * @return true if removal was successful; false otherwise
     */
    boolean removeProduct(String id);

    /**
     * Decreases the quantity of the specified product.
     *
     * @param id the product ID
     * @param quantity the amount to subtract
     * @return true if the quantity was updated successfully; false otherwise
     */
    boolean reduceQuantity(String id, int quantity);

    /**
     * Increases the quantity of the specified product.
     *
     * @param id the product ID
     * @param quantity the amount to add
     * @return true if the quantity was updated successfully; false otherwise
     */
    boolean addQuantity(String id, int quantity);

    /**
     * Retrieves a product using its ID.
     *
     * @param id the product ID
     * @return the matching product; null if not found
     */
    Product getProduct(String id);

    /**
     * Returns a list of all products currently in the inventory.
     *
     * @return a list of all products
     */
    List<Product> getAllProducts();

    /**
     * Checks whether a product exists in the inventory by ID.
     *
     * @param id the product ID
     * @return true if the product exists; false otherwise
     */
    boolean hasProduct(String id);
    
    /**
     * Returns all products with quantity below the given threshold.
     *
     * @param threshold the stock level threshold
     * @return a list of low-stock products
     */
    List<Product> getLowStockProducts(int threshold);
}
