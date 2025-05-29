/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import interfaces.IFileManager;
import interfaces.IInventoryManager;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.ClothingProduct;
import models.Product;
import models.ToyProduct;
import utils.FilePaths;

/**
 * Concrete implementation of IFileManager for all file I/O operations related
 * to the inventory system.
 *
 * Handles saving product data, loading inventory on startup, and exporting
 * filtered data such as low-stock items. Coordinates with product subclasses to
 * accurately reconstruct objects from file contents.
 */
public class FileManager implements IFileManager {

    private final String inventoryFilePath;

    public FileManager(String inventoryFilePath) {
        this.inventoryFilePath = inventoryFilePath;
    }

    /**
     * Saves all products to the inventory file. Overwrites existing content and
     * logs the operation.
     *
     * @param products the map of all products to be saved to file
     */
    @Override
    public void writeProducts(HashMap<String, Product> products) {
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(inventoryFilePath));

            for (Product p : products.values()) {
                pw.println(p.getProductType() + ", " + p.getID() + ", " + p.getName() + ", " + p.getQuantity() + ", " + p.getPrice());
            }

            pw.close();
            LogManager.log("Saved current inventory to file.");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Loads product data from file and restores it into memory. Also resets the
     * product ID counter to prevent ID collisions.
     *
     * @param inventoryManager the manager to populate with loaded products
     */
    @Override
    public void readProducts(IInventoryManager inventoryManager) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(inventoryFilePath));
            String line = null;
            int currentMax = 0;

            while ((line = br.readLine()) != null) {
                String[] info = line.split("\\s*,\\s*");
                String productType = info[0];
                String id = info[1];
                String name = info[2];
                int quantity = Integer.parseInt(info[3]);
                double price = Double.parseDouble(info[4]);

                if (productType.equalsIgnoreCase("Clothing")) {
                    Product product = new ClothingProduct(id, name, quantity, price);
                    inventoryManager.addProduct(product);
                } else if (productType.equalsIgnoreCase("Toy")) {
                    Product product = new ToyProduct(id, name, quantity, price);
                    inventoryManager.addProduct(product);
                }

                // Extract numeric portion of product ID (e.g., "P012" → 12) to track max ID for counter
                int numericId = Integer.parseInt(id.substring(1));
                if (numericId > currentMax) {
                    currentMax = numericId;
                }
            }

            br.close();
            // Ensure future auto-generated product IDs do not overlap with existing ones
            Product.setCounter(currentMax + 1);
            LogManager.log("Loaded products from inventory file.");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Exports products below a given quantity threshold to a low-stock file.
     * Notifies the user if no items are found.
     *
     * @param products the current product map to filter
     * @param threshold the quantity below which products are considered low
     * stock
     */
    @Override
    public void exportLowStock(HashMap<String, Product> products, int threshold) {
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(FilePaths.LOW_STOCK_FILE_PATH));
            int count = 0;

            for (Product p : products.values()) {
                if (p.getQuantity() < threshold) {
                    pw.println(p.getProductType() + ", " + p.getID() + ", "
                            + p.getName() + ", " + p.getQuantity() + ", " + p.getPrice());
                    count++;
                }
            }

            if (count == 0) {
                System.out.println("No products are currently low in stock.");
            } else {
                System.out.println(count + " low-stock product(s) written to low_stock_inventory.txt.");

            }

            pw.close();
            LogManager.log("Exported " + count + " low-stock product(s) below quantity " + threshold + ".");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
