/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.HashMap;
import models.Product;

/**
 * Interface for file I/O operations related to inventory.
 *
 * Supports saving, loading, and exporting inventory data. Enables decoupling
 * from the file system for testing and abstraction.
 */
public interface IFileManager {

    /**
     * Saves all products to the inventory file.
     *
     * @param products the map of products to save
     */
    void writeProducts(HashMap<String, Product> products);

    /**
     * Loads products from the inventory file into memory.
     *
     * @param inventoryManager the inventory target to populate
     */
    void readProducts(IInventoryManager inventoryManager);

    /**
     * Exports low-stock items to a separate report file.
     *
     * @param products the map of products to evaluate
     * @param threshold the minimum quantity considered low stock
     */
    void exportLowStock(HashMap<String, Product> products, int threshold);
}
