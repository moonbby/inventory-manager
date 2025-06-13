/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import static utils.ProductTypes.CLOTHING;

/**
 * Represents a clothing product within the inventory.
 *
 * A concrete subclass of Product with "Clothing" as its predefined type.
 */
public class ClothingProduct extends Product {

    /**
     * Constructs a ClothingProduct with the specified attributes.
     *
     * @param id       the unique product ID
     * @param name     the product name
     * @param quantity the available quantity
     * @param price    the unit price
     */
    public ClothingProduct(String id, String name, int quantity, double price) {
        super(id, name, quantity, price);
    }

    /**
     * Returns the product type associated with this item.
     *
     * @return the string "Clothing"
     */
    @Override
    public String getProductType() {
        return CLOTHING;
    }
}
