/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import interfaces.IInventoryManager;
import java.util.Collection;
import java.util.List;
import models.Product;

/**
 * Concrete implementation of IInventoryManager for in-memory inventory control.
 *
 * Supports standard CRUD operations and inventory access. Acts as the primary
 * interface between the stored product data and system operations.
 */
public class InventoryManager implements IInventoryManager {

    // Stores all products in memory, keyed by unique product ID.
    private final ProductDAO productDAO = new ProductDAO();

    // Adds a product to the inventory by its unique ID.
    @Override
    public Product addProduct(String type, String name, int quantity, double price) {
        return productDAO.addProduct(type, name, quantity, price);
    }

    // Removes a product from the inventory by its ID.
    @Override
    public void removeProduct(String id) {
        productDAO.removeProduct(id);
    }

    // Reduces the quantity of the specified product, preventing negative stock.
    @Override
    public void reduceQuantity(String id, int quantity) {
        Product product = getProduct(id);
        if (product != null) {
            int newQuantity = Math.max(product.getQuantity() - quantity, 0);
            productDAO.updateQuantity(id, newQuantity);
        }
    }

    // Increases the quantity of the specified product.
    @Override
    public void addQuantity(String id, int quantity) {
        Product product = getProduct(id);
        if (product != null) {
            int newQuantity = product.getQuantity() + quantity;
            productDAO.updateQuantity(id, newQuantity);
        }
    }

    // Retrieves a product by its ID.
    @Override
    public Product getProduct(String id) {
        return productDAO.getProduct(id);
    }

    // Returns a collection of all products in the inventory.
    @Override
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    // Checks whether a product with the given ID exists in the inventory.
    @Override
    public boolean hasProduct(String id) {
        return productDAO.getProduct(id) != null;
    }

    @Override
    public List<Product> getLowStockProducts(int threshold) {
        return productDAO.getLowStockProducts(threshold);
    }
}
