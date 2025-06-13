/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 * Abstract superclass representing a product entity in the inventory.
 *
 * Defines shared attributes (ID, name, quantity, price) and enforces
 * type-specific behaviour through getProductType().
 */
public abstract class Product {

    private final String id;
    private String name;
    private int quantity;
    private double price;

    /**
     * Constructs a Product with the specified ID and attributes.
     *
     * @param id the unique product ID
     * @param name the product name
     * @param quantity the available stock count
     * @param price the unit price
     */
    public Product(String id, String name, int quantity, double price) {
        this.id = id;
        setName(name);
        setQuantity(quantity);
        setPrice(price);
    }

    /**
     * Returns the product type string (e.g., "Clothing", "Toy"). Must be
     * implemented by concrete subclasses.
     *
     * @return the product type (e.g., "Clothing", "Toy")
     */
    public abstract String getProductType();

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns a string representation of the product.
     *
     * @return product details as a string
     */
    @Override
    public String toString() {
        return getProductType() + "," + id + "," + name + "," + quantity + "," + price;
    }
}
