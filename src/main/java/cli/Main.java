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
                case 1:
                    addProductMenu();
                    break;
                case 2:
                    removeProductMenu();
                    break;
                case 3:
                    restockProductMenu();
                    break;
                case 4:
                    purchaseProductMenu();
                    break;
                case 5:
                    viewProductsMenu();
                    break;
                case 6:
                    saveExitMenu();
                    break;
                default:
                    System.out.println("Wrong input! Try again.");
                    break;
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

        System.out.println("Enter product name: ");
        String name = scan.nextLine();

        int quantity = -1;
        while (quantity < 0) {
            try {
                System.out.println("Enter quantity: ");
                quantity = scan.nextInt();
                scan.nextLine();
                if (quantity < 0) {
                    System.out.println("Quantity must be non-negative.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scan.nextLine();
            }
        }

        double price = -1;
        while (price < 0) {
            try {
                System.out.println("Enter price: ");
                price = scan.nextDouble();
                scan.nextLine();
                if (price < 0) {
                    System.out.println("Price must be non-negative!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scan.nextLine();
            }
        }

        Product product = null;

        if (type.equalsIgnoreCase("Clothing")) {
            product = new ClothingProduct(name, quantity, price);
        } else if (type.equalsIgnoreCase("Toy")) {
            product = new ToyProduct(name, quantity, price);
        }

        inventoryManager.addProduct(product);
        System.out.println("Product added successfully");
    }

    public void removeProductMenu() {
        System.out.println("Enter the ID of the product to remove (e.g., P001): ");
        String id = scan.nextLine();

        while (true) {
            if (inventoryManager.hasProduct(id)) {
                inventoryManager.removeProduct(id);
                System.out.println("Product removed successfully.");
                break;
            } else {
                System.out.println("The product with that ID does not exist! Try again: ");
                id = scan.nextLine();
            }
        }
    }

    public void restockProductMenu() {
        System.out.println("Enter the ID of the product to restock (e.g., P001): ");
        String id = scan.nextLine();

        while (true) {
            if (!inventoryManager.hasProduct(id)) {
                System.out.println("The product with that ID does not exist! Try again: ");
                id = scan.nextLine();
            } else {
                break;
            }
        }

        int quantity = -1;
        while (quantity < 1) {
            try {
                System.out.println("Enter the quantity of the product to restock: ");
                quantity = scan.nextInt();
                scan.nextLine();

                if (quantity < 1) {
                    System.out.println("Quantity must be at least 1!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scan.nextLine();
            }
        }

        inventoryManager.addQuantity(id, quantity);
        System.out.println("Product restocked successfully.");
    }

    public void purchaseProductMenu() {
        System.out.println("Enter the ID of the product to purchase (e.g., P001): ");
        String id = scan.nextLine();

        while (true) {
            if (!inventoryManager.hasProduct(id)) {
                System.out.println("The product with that ID does not exist! Try again: ");
                id = scan.nextLine();
            } else {
                break;
            }
        }

        int quantity = -1;
        while (quantity < 1) {
            try {
                System.out.println("Enter the quantity of the product to purchase: ");
                quantity = scan.nextInt();
                scan.nextLine();

                if (quantity < 1) {
                    System.out.println("Quantity must be at least 1!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scan.nextLine();
            }
        }

        Product product = inventoryManager.getProduct(id);
        while (true) {
            if (product.getQuantity() < quantity) {
                System.out.println("Not enough stock available! Try again:");
                quantity = scan.nextInt();
                scan.nextLine();
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
}
