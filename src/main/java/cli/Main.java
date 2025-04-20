/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cli;

import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Scanner;
import managers.FileManager;
import managers.InventoryManager;
import models.ClothingProduct;
import models.Product;
import models.ToyProduct;

/**
 * Command-line interface for interacting with the inventory system. Handles
 * user input and maps options to appropriate functionality.
 */
public class Main {

    private static InventoryManager inventoryManager = new InventoryManager();
    private static FileManager fileManager = new FileManager();
    private final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        fileManager.readProducts(inventoryManager);
        Main app = new Main();
        app.run();
    }

    public void run() {
        while (true) {
            printMenu();
            handleUserInput();
        }
    }

    public void printMenu() {
        System.out.println("Welcome to the inventory management system!");
        System.out.println("How can I help you? Type a number.");
        System.out.println("Option 1: Add a new product");
        System.out.println("Option 2: Remove an existing product");
        System.out.println("Option 3: Restock a product");
        System.out.println("Option 4: Purchase a product");
        System.out.println("Option 5: View all products");
        System.out.println("Option 6: Save and Exit");
    }

    public void handleUserInput() {
        try {
            int input = scan.nextInt();
            scan.nextLine();

            switch (input) {
                case 1 ->
                    addProductMenu();
                case 2 ->
                    removeProductMenu();
                case 3 ->
                    restockProductMenu();
                case 4 ->
                    purchaseProductMenu();
                case 5 ->
                    viewProductsMenu();
                case 6 ->
                    saveExitMenu();
                default ->
                    System.out.println("Wrong input! Input must be between 1 to 6. Try again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            scan.nextLine();
        }
    }

    public void addProductMenu() {
        String type = "";

        while (true) {
            System.out.println("Enter product type (Clothing or Toy): ");
            type = scan.nextLine();

            if (!(type.equalsIgnoreCase("Clothing")) && !(type.equalsIgnoreCase("Toy"))) {
                System.out.println("Error! Invalid product type!");
            } else {
                break;
            }
        }

        // Prompt until a valid non-empty product name is entered
        String name;
        while (true) {
            System.out.println("Enter product name:");
            name = scan.nextLine();
            if (!name.trim().isEmpty()) {
                break;
            }
            System.out.println("Product name cannot be empty.");
        }

        int quantity = promptInt("Enter quantity: ", 0);
        double price = promptDouble("Enter price: ", 0);

        Product product = null;

        // Determine product type based on user input
        if (type.equalsIgnoreCase("Clothing")) {
            product = new ClothingProduct(name, quantity, price);
        } else if (type.equalsIgnoreCase("Toy")) {
            product = new ToyProduct(name, quantity, price);
        }

        inventoryManager.addProduct(product);
        System.out.println("Product added successfully");
    }

    public void removeProductMenu() {
        String id = promptValidProductID("Enter the ID of the product to remove (e.g., P001):");
        inventoryManager.removeProduct(id);
        System.out.println("Product removed successfully.");
    }

    public void restockProductMenu() {
        String id = promptValidProductID("Enter the ID of the product to restock (e.g., P001):");
        int quantity = promptInt("Enter the quantity of the product to restock: ", 1);
        inventoryManager.addQuantity(id, quantity);
        System.out.println("Product restocked successfully.");
    }

    public void purchaseProductMenu() {
        String id = promptValidProductID("Enter the ID of the product to purchase (e.g., P001):");
        Product product = inventoryManager.getProduct(id);
        int quantity;

        while (true) {
            quantity = promptInt("Enter the quantity of the product to purchase: ", 1);
            if (product.getQuantity() < quantity) {
                System.out.println("Not enough stock available! Try again:");
            } else {
                break;
            }
        }

        inventoryManager.reduceQuantity(id, quantity);
        System.out.println("Product purchased successfully.");
    }

    public void viewProductsMenu() {
        Collection<Product> products = inventoryManager.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("No products in inventory.");
            return;
        }

        for (Product p : products) {
            System.out.println("Type: " + p.getProductType() + " | ID: " + p.getID() + " | Name: " + p.getName() + " | Quantity: " + p.getQuantity() + " | Price: " + p.getPrice());
        }
    }

    public void saveExitMenu() {
        fileManager.writeProducts(inventoryManager.getProductMap());
        System.out.println("File is saved successfully!");
        System.exit(0);
    }

    // =========================
    //       HELPER METHODS
    // =========================
    
    // Prompts for a product ID and ensures it exists in inventory
    public String promptValidProductID(String message) {
        System.out.println(message);
        String id = scan.nextLine();
        while (!inventoryManager.hasProduct(id)) {
            System.out.println("The product with that ID does not exist! Try again:");
            id = scan.nextLine();
        }
        return id;
    }

    // Prompts for a numeric input with minimum value validation and error handling
    public int promptInt(String message, int minValue) {
        int value = -1;
        while (value < minValue) {
            try {
                System.out.println(message);
                value = scan.nextInt();
                scan.nextLine();
                if (value < minValue) {
                    System.out.println("Input must be at least " + minValue);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scan.nextLine();
            }
        }
        return value;
    }

    // Prompts for a numeric input with minimum value validation and error handling
    public double promptDouble(String message, double minValue) {
        double value = -1;
        while (value < minValue) {
            try {
                System.out.println(message);
                value = scan.nextDouble();
                scan.nextLine();
                if (value < minValue) {
                    System.out.println("Input must be at least " + minValue);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scan.nextLine();
            }
        }
        return value;
    }
}