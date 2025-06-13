/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import models.Product;

/**
 * Interface for modifying product data in an inventory source.
 *
 * Defines write-only operations for adding, removing, or updating inventory
 * items.
 */
public interface IProductWriter {

    /**
     * Adds a new product to the inventory.
     *
     * @param type the product type (e.g. Clothing, Toy)
     * @param name the name of the product
     * @param quantity the initial stock quantity
     * @param price the product price
     * @return the added Product instance; null if the operation failed
     */
    Product addProduct(String type, String name, int quantity, double price);

    /**
     * Removes a product from the inventory based on its ID.
     *
     * @param id the unique identifier of the product
     * @return true if the product was successfully removed; false otherwise
     */
    boolean removeProduct(String id);

    /**
     * Updates the quantity of a specific product.
     *
     * @param id the unique identifier of the product
     * @param newQuantity the new quantity to assign
     * @return true if the update was successful; false otherwise
     */
    boolean updateQuantity(String id, int newQuantity);
}
