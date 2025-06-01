/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.List;
import models.Product;

/**
 *
 * @author lifeo
 */
public class CLIPrinter {

    public static void printProducts(List<Product> products) {
        
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }

        System.out.println("\n=== Product List ===");
        for (Product p : products) {
            System.out.printf("Type: %s | ID: %s | Name: %s | Qty: %d | Price: $%.2f%n",
                    p.getProductType(), p.getID(), p.getName(),
                    p.getQuantity(), p.getPrice());
        }
    }
}
