/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cli;

import interfaces.IInputHelper;
import interfaces.IInventoryManager;
import java.util.Collection;
import managers.LogManager;
import models.ClothingProduct;
import models.Product;
import models.ToyProduct;

/**
 * Handles all product-related menu operations.
 *
 * Acts as the interface between user input and inventory logic, coordinating
 * with InputHelper for validation and InventoryManager for state management.
 */
public class ProductMenuController {

    private final IInventoryManager inventoryManager;
    private final IInputHelper inputHelper;

    public ProductMenuController(IInventoryManager inventoryManager, IInputHelper inputHelper) {
        this.inventoryManager = inventoryManager;
        this.inputHelper = inputHelper;
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

            if (!(type.equalsIgnoreCase("Clothing")) && !(type.equalsIgnoreCase("Toy"))) {
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

        double price = inputHelper.promptDouble("Enter price: ", 0);
        if (price == -1) {
            System.out.println("Cancelled.");
            return;
        }

        Product product = null;

        if (type.equalsIgnoreCase("Clothing")) {
            product = new ClothingProduct(name, quantity, price);
        } else if (type.equalsIgnoreCase("Toy")) {
            product = new ToyProduct(name, quantity, price);
        } else {
            // This should never happen due to validation above
            throw new IllegalStateException("Unexpected product type: " + type);
        }

        inventoryManager.addProduct(product);
        System.out.println("Product added successfully");
        LogManager.log("Added " + product.getProductType() + " " + product.getID()
                + ": " + product.getName() + " (Qty: " + product.getQuantity()
                + ", $" + product.getPrice() + ")");
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

        Product removed = inventoryManager.getProduct(id);
        inventoryManager.removeProduct(id);
        System.out.println("Product removed successfully.");
        LogManager.log("Removed " + removed.getProductType()
                + " " + removed.getID() + ": " + removed.getName());
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
        System.out.println("Product restocked successfully. Updated stock: " + product.getQuantity());

        LogManager.log("Restocked " + quantity + " of "
                + product.getProductType() + " " + product.getID()
                + ": " + product.getName());
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

        LogManager.log("Purchased " + quantity + " of "
                + product.getProductType() + " " + product.getID()
                + ": " + product.getName());
    }

    /**
     * Displays a list of all products currently in inventory.
     *
     * Shows product type, ID, name, quantity, and price. Displays a message if
     * inventory is empty.
     */
    public void viewProductsMenu() {
        Collection<Product> products = inventoryManager.getAllProducts();
        LogManager.log("Viewed all products.");

        if (products.isEmpty()) {
            System.out.println("No products in inventory.");
            return;
        }

        for (Product p : products) {
            System.out.println("Type: " + p.getProductType() + " | ID: " + p.getID()
                    + " | Name: " + p.getName() + " | Quantity: " + p.getQuantity()
                    + " | Price: " + p.getPrice());
        }
    }

}
