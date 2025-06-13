/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import interfaces.IInventoryManager;
import java.util.List;
import models.Product;
import utils.ProductTypes;

/**
 * Database-backed implementation of IInventoryManager for inventory operations.
 *
 * Provides core logic for managing inventory records, including validation and
 * CRUD operations. Delegates database persistence to the underlying ProductDAO.
 */
public class InventoryManager implements IInventoryManager {

    /**
     * Handles low-level database operations for product records.
     */
    private final ProductDAO productDAO = new ProductDAO();

    /**
     * Adds a new product to the inventory if the provided values are valid.
     *
     * @param type the product type (must be Clothing or Toy)
     * @param name the product name (non-null, non-empty)
     * @param quantity the starting quantity (must be 0 or greater)
     * @param price the product price (must be greater than 0)
     * @return the newly added Product if successful; null otherwise
     */
    @Override
    public Product addProduct(String type, String name, int quantity, double price) {
        if (type == null || (!type.equalsIgnoreCase(ProductTypes.CLOTHING) && !type.equalsIgnoreCase(ProductTypes.TOY))) {
            return null;
        }
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        if (quantity < 0 || price <= 0) {
            return null;
        }

        return productDAO.addProduct(type, name, quantity, price);
    }

    /**
     * Removes a product from the inventory by its unique ID.
     *
     * @param id the product ID
     * @return true if removed successfully; false otherwise
     */
    @Override
    public boolean removeProduct(String id) {
        return productDAO.removeProduct(id);
    }

    /**
     * Decreases the quantity of the given product. Will not reduce stock below
     * zero.
     *
     * @param id the product ID
     * @param quantity the amount to deduct
     * @return true if updated successfully; false if product not found or
     * quantity invalid
     */
    @Override
    public boolean reduceQuantity(String id, int quantity) {
        if (quantity < 0) {
            return false;
        }

        Product product = getProduct(id);
        if (product != null) {
            int newQuantity = Math.max(product.getQuantity() - quantity, 0);
            return productDAO.updateQuantity(id, newQuantity);
        }

        return false;
    }

    /**
     * Increases the quantity of the given product.
     *
     * @param id the product ID
     * @param quantity the amount to add (must be positive)
     * @return true if updated successfully; false otherwise
     */
    @Override
    public boolean addQuantity(String id, int quantity) {
        if (quantity <= 0) {
            return false;
        }

        Product product = getProduct(id);
        if (product != null) {
            int newQuantity = product.getQuantity() + quantity;
            return productDAO.updateQuantity(id, newQuantity);
        }
        return false;
    }

    /**
     * Retrieves a product by its ID from the database.
     *
     * @param id the product ID
     * @return the matching Product if found; null otherwise
     */
    @Override
    public Product getProduct(String id) {
        return productDAO.getProduct(id);
    }

    /**
     * Retrieves all products currently stored in the database.
     *
     * @return a list of all products
     */
    @Override
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    /**
     * Checks whether a product with the given ID exists in the inventory.
     *
     * @param id the product ID
     * @return true if the product exists; false otherwise
     */
    @Override
    public boolean hasProduct(String id) {
        return productDAO.getProduct(id) != null;
    }

    /**
     * Retrieves all products with stock below the given threshold.
     *
     * @param threshold the stock level to compare against
     * @return a list of low-stock products
     */
    @Override
    public List<Product> getLowStockProducts(int threshold) {
        return productDAO.getLowStockProducts(threshold);
    }
}
