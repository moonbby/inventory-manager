/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cli;

import interfaces.IInputHelper;
import interfaces.IInventoryManager;
import java.util.Collection;
import java.util.List;
import managers.LogManager;
import models.Product;
import utils.CLIPrinter;
import utils.ProductTypes;

/**
 * Handles all product-related menu operations.
 *
 * Acts as the interface between user input and inventory logic, coordinating
 * with InputHelper for validation and InventoryManager for state management.
 */
public class ProductMenuController {

    private final IInventoryManager inventoryManager;
    private final IInputHelper inputHelper;
    private final LogManager logManager;

    public ProductMenuController(IInventoryManager inventoryManager,
            IInputHelper inputHelper, LogManager logManager) {
        this.inventoryManager = inventoryManager;
        this.inputHelper = inputHelper;
        this.logManager = logManager;
    }

    /**
     * Prompts the user to add a new product to the inventory.
     *
     * Validates type, name, quantity, and price. Constructs and stores either a
     * ClothingProduct or ToyProduct.
     */
    public void addProductMenu() {
        String type = "";

        while (true) {
            type = inputHelper.promptString("Enter product type (Clothing or Toy): ");
            if (type == null) {
                System.out.println("Cancelled.");
                return;
            }

            if (!(type.equalsIgnoreCase(ProductTypes.CLOTHING)) && !(type.equalsIgnoreCase(ProductTypes.TOY))) {
                System.out.println("Error! Invalid product type! Try again.");
            } else {
                break;
            }
        }

        String name;
        while (true) {
            name = inputHelper.promptString("Enter product name:");
            if (name == null) {
                System.out.println("Cancelled.");
                return;
            }

            if (!name.trim().isEmpty()) {
                break;
            }
            System.out.println("Product name cannot be empty.");
        }

        int quantity = inputHelper.promptInt("Enter quantity: ", 0);
        if (quantity == -1) {
            System.out.println("Cancelled.");
            return;
        }

        double price = inputHelper.promptDouble("Enter price: ", 0.01);
        if (price == -1) {
            System.out.println("Cancelled.");
            return;
        }

        Product product = inventoryManager.addProduct(type, name, quantity, price);

        if (product == null) {
            System.out.println("Failed to add product due to internal error.");
            return;
        }
        
        System.out.println("Product added successfully");
    }

    /**
     * Removes an existing product from the inventory by ID.
     *
     * Validates the entered ID and deletes the corresponding product.
     */
    public void removeProductMenu() {
        String id = inputHelper.promptValidProductID("Enter the ID of the product to remove (e.g., P001):");
        if (id == null) {
            System.out.println("Cancelled.");
            return;
        }

        inventoryManager.removeProduct(id);
        System.out.println("Product removed successfully.");
    }

    /**
     * Restocks an existing product in the inventory.
     *
     * Prompts for a valid product ID and restock quantity, then updates
     * inventory.
     */
    public void restockProductMenu() {
        String id = inputHelper.promptValidProductID("Enter the ID of the product to restock (e.g., P001):");
        if (id == null) {
            System.out.println("Cancelled.");
            return;
        }

        Product product = inventoryManager.getProduct(id);

        System.out.println("Current stock: " + product.getQuantity());
        int quantity = inputHelper.promptInt("Enter the quantity of the product to restock: ", 1);
        if (quantity == -1) {
            System.out.println("Cancelled.");
            return;
        }

        inventoryManager.addQuantity(id, quantity);
        product = inventoryManager.getProduct(id);
        System.out.println("Product restocked successfully. Updated stock: " + product.getQuantity());
    }

    /**
     * Processes a product purchase by reducing its stock.
     *
     * Validates input, ensures sufficient quantity, and updates inventory.
     */
    public void purchaseProductMenu() {
        String id = inputHelper.promptValidProductID("Enter the ID of the product to purchase (e.g., P001):");
        if (id == null) {
            System.out.println("Cancelled.");
            return;
        }
        Product product = inventoryManager.getProduct(id);
        int quantity;

        while (true) {
            System.out.println("Current stock: " + product.getQuantity());
            quantity = inputHelper.promptInt("Enter the quantity to purchase: ", 1);
            if (quantity == -1) {
                System.out.println("Cancelled.");
                return;
            }
            if (product.getQuantity() < quantity) {
                System.out.println("Not enough stock available! Try again.");
            } else {
                break;
            }
        }

        inventoryManager.reduceQuantity(id, quantity);
        System.out.println("Product purchased successfully.");
    }

    /**
     * Displays a list of all products currently in inventory.
     *
     * Shows product type, ID, name, quantity, and price. Displays a message if
     * inventory is empty.
     */
    public void showProductsMenu() {
        List<Product> products = inventoryManager.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("No products in inventory.");
            return;
        }

        CLIPrinter.printProducts(products);
    }

}
