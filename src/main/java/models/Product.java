/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;


/**
 * Abstract class representing a generic product in the inventory.
 * Implements basic attributes like ID, name, quantity, and price.
 * Subclasses must specify the product type.
 * Demonstrates abstraction and polymorphism.
 */


public abstract class Product {

    private final String id;
    private String name;
    private int quantity;
    private double price;
    private static int counter = 1;
    
    public Product(String id, String name, int quantity, double price) {
        this.id = id;
        setName(name);
        setQuantity(quantity);
        setPrice(price);
    }
    
    public Product(String name, int quantity, double price) {
        this.id = String.format("P%03d", counter++);
        setName(name);
        setQuantity(quantity);
        setPrice(price);
    }

    public abstract String getProductType();

    public static void setCounter(int value) {
        counter = value;
    }

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

    @Override
    public String toString() {
        return getProductType() + "," + name + "," + price + "," + quantity + "," + id;
    }

}
