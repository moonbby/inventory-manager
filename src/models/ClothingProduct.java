/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import static utils.ProductTypes.CLOTHING;

/**
 * Represents a clothing product within the inventory.
 *
 * Extends the abstract Product class and defines "Clothing" as its type.
 * Supports both manual ID assignment (for file loading) and auto-generated IDs
 * (for new entries).
 */
public class ClothingProduct extends Product {

    // Constructs a ClothingProduct with a predefined ID, typically used during file loading.
    public ClothingProduct(String id, String name, int quantity, double price) {
        super(id, name, quantity, price);
    }

    // Returns the type of this product.
    @Override
    public String getProductType() {
        return CLOTHING;
    }
}
