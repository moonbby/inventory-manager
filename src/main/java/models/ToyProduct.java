/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 * Concrete subclass of Product representing toy items.
 * Implements the getProductType method to return "Toy".
 */

public class ToyProduct extends Product {
    
    public ToyProduct(String id, String name, int quantity, double price) {
        super(id, name, quantity, price);
    }
    
    public ToyProduct(String name, int quantity, double price) {
        super(name, quantity, price);
    }
    
    @Override
    public String getProductType() {
        return "Toy";
    }
}
