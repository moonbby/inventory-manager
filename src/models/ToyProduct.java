/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import static utils.ProductTypes.TOY;

/**
 * Represents a toy product within the inventory.
 *
 * Extends the abstract Product class and defines "Toy" as its type. Supports
 * both manual ID assignment (for file loading) and auto-generated IDs (for new
 * entries).
 */
public class ToyProduct extends Product {

    // Constructs a ToyProduct with a predefined ID, typically used during file loading.
    public ToyProduct(String id, String name, int quantity, double price) {
        super(id, name, quantity, price);
    }

    // Returns the type of this product.
    @Override
    public String getProductType() {
        return TOY;
    }
}
