/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

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


/**
 * Handles file input/output operations.
 * Responsible for saving inventory data to and reading from a text file.
 * Ensures inventory persists across program runs.
 */

public class FileManager {

    public void writeProducts(HashMap<String, Product> products) {
        try {
            FileOutputStream fos = new FileOutputStream("./resources/inventory.txt");
            PrintWriter pw = new PrintWriter(fos);

            for (Product p : products.values()) {
                pw.println(p.getProductType() + ", " + p.getID() + ", " + p.getName() + ", " + p.getQuantity() + ", " + p.getPrice());
            }

            System.out.println("Written to inventory.txt.");
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void readProducts(InventoryManager inventoryManager) {
        try {
            FileReader fr = null;

            fr = new FileReader("./resources/inventory.txt");
            BufferedReader br = new BufferedReader(fr);
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

                int numericId = Integer.parseInt(id.substring(1));
                if (numericId > currentMax) {
                    currentMax = numericId;
                }
            }

            Product.setCounter(currentMax + 1);
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
