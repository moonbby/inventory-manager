/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import java.util.Collection;
import models.Product;

/**
 * Generates analytical reports based on current inventory data.
 *
 * Provides category-based summaries and identifies the most expensive product.
 * Outputs are printed to the console and logged for traceability.
 */
public class ReportManager {

    /**
     * Prints a summary of product counts by category.
     *
     * @param products the current list of inventory items
     */
    public static void viewSummaryReport(Collection<Product> products) {
        int totalCount = 0;
        int clothingCount = 0;
        int toyCount = 0;

        for (Product p : products) {
            totalCount++;
            if (p.getProductType().equalsIgnoreCase("Clothing")) {
                clothingCount++;
            } else if (p.getProductType().equalsIgnoreCase("Toy")) {
                toyCount++;
            }
        }

        System.out.println("\n=== Inventory Summary Report ===");
        System.out.println("Total products: " + totalCount);
        System.out.println("Clothing products: " + clothingCount);
        System.out.println("Toy products: " + toyCount);
        LogManager.log("Viewed summary report.");
    }

    /**
     * Displays the most expensive product in the inventory.
     *
     * Prints product details or a warning if the inventory is empty.
     *
     * @param products the current list of inventory items
     */
    public static void viewMostExpensiveProduct(Collection<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        Product max = null;
        for (Product p : products) {
            if (max == null || p.getPrice() > max.getPrice()) {
                max = p;
            }
        }

        System.out.println("\n=== Most Expensive Product ===");
        System.out.println("Type: " + max.getProductType());
        System.out.println("ID: " + max.getID());
        System.out.println("Name: " + max.getName());
        System.out.println("Price: $" + max.getPrice());
        System.out.println("Quantity: " + max.getQuantity());
        LogManager.log("Viewed most expensive product.");
    }
}
