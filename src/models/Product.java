/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 * Abstract superclass representing a generic product in the inventory.
 *
 * Encapsulates shared attributes such as ID, name, quantity, and price.
 * Subclasses must specify the product type by implementing getProductType().
 */
public abstract class Product {

    private final String id;
    private String name;
    private int quantity;
    private double price;

    // Constructs a product with a predefined ID, typically used during file loading.
    public Product(String id, String name, int quantity, double price) {
        this.id = id;
        setName(name);
        setQuantity(quantity);
        setPrice(price);
    }

    /**
     * Returns the product type. Must be implemented by subclasses.
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

    // Returns a string representation of the product.
    @Override
    public String toString() {
        return getProductType() + "," + id + "," + name + "," + quantity + "," + price;
    }

}
