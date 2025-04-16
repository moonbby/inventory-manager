/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 * Concrete subclass of Product representing clothing items.
 * Implements the getProductType method to return "Clothing".
 */


public class ClothingProduct extends Product {

    public ClothingProduct(String id, String name, int quantity, double price) {
        super(id, name, quantity, price);
    }
    
    public ClothingProduct(String name, int quantity, double price) {
        super(name, quantity, price);
    }

    @Override
    public String getProductType() {
        return "Clothing";
    }
}
