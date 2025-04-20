/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import java.util.Collection;
import java.util.HashMap;
import models.Product;

/**
 * Class responsible for managing the in-memory product inventory.
 * Provides methods for CRUD on products.
 * Acts as the primary interface between the CLI and product data structure.
 */


public class InventoryManager {

    // In-memory storage of all products using unique product ID as key
    private HashMap<String, Product> products = new HashMap<>();

    // Adds a new product to the inventory
    public void addProduct(Product product) {
        products.put(product.getID(), product);
    }

    // Removes a product by ID
    public void removeProduct(String id) {
        products.remove(id);
    }

    // Decreases quantity of a product, ensuring it doesn't go below zero
    public void reduceQuantity(String id, int quantity) {
        if (products.containsKey(id)) {
            Product p = products.get(id);
            p.setQuantity(Math.max(p.getQuantity() - quantity, 0));
        }
    }

    // Increases quantity of a product
    public void addQuantity(String id, int quantity) {
        if (products.containsKey(id)) {
            Product p = products.get(id);
            p.setQuantity(p.getQuantity() + quantity);
        }
    }

    // Returns product object by ID
    public Product getProduct(String id) {
        return products.get(id);
    }

    // Returns all products in inventory
    public Collection<Product> getAllProducts() {
        return products.values();
    }

    public HashMap<String, Product> getProductMap() {
        return products;
    }

    public boolean hasProduct(String id) {
        return products.containsKey(id);
    }
}
