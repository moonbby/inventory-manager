/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import interfaces.IInventoryManager;
import java.util.Collection;
import java.util.HashMap;
import models.Product;

/**
 * Concrete implementation of IInventoryManager for in-memory inventory control.
 *
 * Supports standard CRUD operations and inventory access. Acts as the primary
 * interface between the stored product data and system operations.
 */
public class InventoryManager implements IInventoryManager {

    // Stores all products in memory, keyed by unique product ID.
    private final HashMap<String, Product> products = new HashMap<>();

    // Adds a product to the inventory by its unique ID.
    @Override
    public void addProduct(Product product) {
        products.put(product.getID(), product);
    }

    // Removes a product from the inventory by its ID.
    @Override
    public void removeProduct(String id) {
        products.remove(id);
    }

    // Reduces the quantity of the specified product, preventing negative stock.
    @Override
    public void reduceQuantity(String id, int quantity) {
        if (products.containsKey(id)) {
            Product p = products.get(id);
            p.setQuantity(Math.max(p.getQuantity() - quantity, 0));
        }
    }

    // Increases the quantity of the specified product.
    @Override
    public void addQuantity(String id, int quantity) {
        if (products.containsKey(id)) {
            Product p = products.get(id);
            p.setQuantity(p.getQuantity() + quantity);
        }
    }

    // Retrieves a product by its ID.
    @Override
    public Product getProduct(String id) {
        return products.get(id);
    }

    // Returns a collection of all products in the inventory.
    @Override
    public Collection<Product> getAllProducts() {
        return products.values();
    }

    // Returns the full internal map of product IDs to Product objects.
    @Override
    public HashMap<String, Product> getProductMap() {
        return products;
    }

    // Checks whether a product with the given ID exists in the inventory.
    @Override
    public boolean hasProduct(String id) {
        return products.containsKey(id);
    }
}
