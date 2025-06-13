/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import models.Product;

/**
 * Interface for retrieving product data from an inventory source.
 *
 * Defines read-only operations for accessing products by ID, fetching all
 * items, and filtering products based on stock thresholds.
 */
public interface IProductReader {

    /**
     * Retrieves a single product by its unique identifier.
     *
     * @param id the product ID to look up
     * @return the matching Product; null if not found
     */
    Product getProduct(String id);

    /**
     * Retrieves all products available in the inventory source.
     *
     * @return a list of all products; an empty list if none exist
     */
    List<Product> getAllProducts();

    /**
     * Retrieves products whose quantity is below the specified threshold.
     *
     * @param threshold the quantity limit for low stock
     * @return a list of products with stock less than the threshold
     */
    List<Product> getLowStockProducts(int threshold);
}
