/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import interfaces.IProductReader;
import interfaces.IProductWriter;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import models.ClothingProduct;
import models.Product;
import models.ToyProduct;
import static utils.DBConstants.*;
import utils.ProductFactory;
import utils.ProductTypes;

/**
 * Handles direct database operations for product records. Logs the result of
 * the operation.
 *
 * Implements both read and write interfaces to support full CRUD functionality.
 */
public class ProductDAO implements IProductReader, IProductWriter {

    private final Connection conn = DatabaseManager.getInstance().getConnection();
    private final LogManager logManager = new LogManager();

    /**
     * Adds a new product to the database with a generated ID.
     *
     * @param type the product type (Clothing or Toy)
     * @param name the product name
     * @param quantity the initial quantity
     * @param price the price
     * @return the created Product; null if the insert failed
     */
    @Override
    public Product addProduct(String type, String name, int quantity, double price) {
        Product product = null;
        try {
            String id = generateNextId();

            if (type.equalsIgnoreCase(ProductTypes.CLOTHING)) {
                product = new ClothingProduct(id, name, quantity, price);
            } else if (type.equalsIgnoreCase(ProductTypes.TOY)) {
                product = new ToyProduct(id, name, quantity, price);
            } else {
                // Fallback safety: product type should be validated upstream, but enforce here to guarantee correctness
                throw new IllegalArgumentException("Invalid product type");
            }

            String sql = "INSERT INTO " + TABLE_PRODUCTS + " VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, product.getID());
            ps.setString(2, product.getName());
            ps.setInt(3, product.getQuantity());
            ps.setDouble(4, product.getPrice());
            ps.setString(5, product.getProductType());
            ps.executeUpdate();
            ps.close();

            logManager.log("Added", "product " + product.getID(), true, null);
        } catch (SQLException ex) {
            logManager.log("Add", "product", false, ex.getMessage());
        }
        return product;
    }

    /**
     * Removes a product from the database by ID. Logs success or failure,
     * including reason if not found.
     *
     * @param id the product ID
     * @return true if the product was removed; false otherwise
     */
    @Override
    public boolean removeProduct(String id) {
        try {
            String sql = "DELETE FROM " + TABLE_PRODUCTS + " WHERE " + COL_PRODUCT_ID + " = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, id);
            int affected = ps.executeUpdate();
            ps.close();

            if (affected > 0) {
                logManager.log("Removed", "product " + id, true, null);
            } else {
                logManager.log("Remove", "product " + id, false, "No matching product found");
            }

            return affected > 0;
        } catch (SQLException ex) {
            logManager.log("Remove", "product " + id, false, ex.getMessage());
            return false;
        }
    }

    /**
     * Updates a product's quantity in the database. Fetches the current value,
     * applies changes, and logs how the quantity changed.
     *
     * @param id the product ID
     * @param newQuantity the new quantity value
     * @return true if update succeeded; false otherwise
     */
    @Override
    public boolean updateQuantity(String id, int newQuantity) {
        try {
            String selectSql = "SELECT " + COL_QUANTITY + " FROM " + TABLE_PRODUCTS + " WHERE " + COL_PRODUCT_ID + " = ?";
            PreparedStatement selectPs = conn.prepareStatement(selectSql);
            selectPs.setString(1, id);
            ResultSet rs = selectPs.executeQuery();

            int currentQuantity = -1;
            if (rs.next()) {
                currentQuantity = rs.getInt(COL_QUANTITY);
            }
            rs.close();
            selectPs.close();

            if (currentQuantity == -1) {
                logManager.log("Update", "quantity for product " + id, false, "Product not found.");
                return false;
            }

            String updateSql = "UPDATE " + TABLE_PRODUCTS + " SET " + COL_QUANTITY + " = ? WHERE " + COL_PRODUCT_ID + " = ?";
            PreparedStatement updatePs = conn.prepareStatement(updateSql);
            updatePs.setInt(1, newQuantity);
            updatePs.setString(2, id);
            updatePs.executeUpdate();
            updatePs.close();

            if (newQuantity > currentQuantity) {
                logManager.log("Restocked", "product " + id + " (+"
                        + (newQuantity - currentQuantity) + ")", true, null);
            } else if (newQuantity < currentQuantity) {
                logManager.log("Purchased", "product " + id + " (-"
                        + (currentQuantity - newQuantity) + ")", true, null);
            } else {
                logManager.log("Updated", "product " + id + " (no quantity change)", true, null);
            }

            return true;
        } catch (SQLException ex) {
            logManager.log("Update", "quantity for product " + id, false, ex.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a product by ID from the database. Logs whether the product was
     * found and returned.
     *
     * @param id the product ID
     * @return the Product object; null if not found
     */
    @Override
    public Product getProduct(String id) {
        Product product = null;
        try {
            String sql = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_PRODUCT_ID + " = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                product = ProductFactory.createFromResultSet(rs);
            }

            ps.close();
            rs.close();

            if (product != null) {
                logManager.log("Fetched", "product " + id, true, null);
            } else {
                logManager.log("Fetch", "product " + id, false, "Product not found.");
            }
        } catch (SQLException ex) {
            logManager.log("Fetch", "product " + id, false, ex.getMessage());
        }
        return product;
    }

    /**
     * Returns all products currently stored in the database.
     *
     * @return a list of all products
     */
    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + TABLE_PRODUCTS;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product product = ProductFactory.createFromResultSet(rs);
                products.add(product);
            }
            ps.close();
            rs.close();

            logManager.log("Fetched", "all products", true, null);
        } catch (SQLException ex) {
            logManager.log("Fetch", "all products", false, ex.getMessage());
        }
        return products;
    }

    /**
     * Retrieves all products below the given stock threshold.
     *
     * @param threshold the stock limit
     * @return list of products under the threshold
     */
    @Override
    public List<Product> getLowStockProducts(int threshold) {
        List<Product> products = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_QUANTITY + " < ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, threshold);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product product = ProductFactory.createFromResultSet(rs);
                products.add(product);
            }
            ps.close();
            rs.close();

            logManager.log("Fetched", "low stock products (threshold: " + threshold + ")", true, null);
        } catch (SQLException ex) {
            logManager.log("Fetch", "low stock products", false, ex.getMessage());
        }
        return products;
    }

    /**
     * Generates the next available product ID using sequential format. Based on
     * max ID currently in the database.
     *
     * @return the next product ID string, or fallback if failed
     */
    private String generateNextId() {
        try {
            String sql = "SELECT MAX(" + COL_PRODUCT_ID + ") AS MAX_ID FROM " + TABLE_PRODUCTS;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            int maxID = 0;
            if (rs != null && rs.next()) {
                String lastID = rs.getString("MAX_ID");

                try {
                    if (lastID != null && lastID.matches("P\\d+")) {
                        maxID = Integer.parseInt(lastID.substring(1));
                    }
                } catch (NumberFormatException e) {
                    logManager.log("Generate", "new product ID", false, "Invalid ID format in DB: " + lastID);
                }
            }

            rs.close();
            ps.close();
            return String.format("P%03d", maxID + 1);
        } catch (SQLException ex) {
            logManager.log("Generate", "new product ID", false, ex.getMessage());
            return "P_ERR"; // fallback
        }
    }
}
