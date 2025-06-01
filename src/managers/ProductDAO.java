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
import static utils.LoggerUtil.logStatus;
import utils.ProductFactory;

/**
 *
 * @author lifeo
 */
public class ProductDAO implements IProductReader, IProductWriter {

    private final Connection conn = DatabaseManager.getInstance().getConnection();

    @Override
    public Product addProduct(String type, String name, int quantity, double price) {
        Product product = null;
        try {
            String id = generateNextId();

            if (type.equalsIgnoreCase("Clothing")) {
                product = new ClothingProduct(id, name, quantity, price);
            } else if (type.equalsIgnoreCase("Toy")) {
                product = new ToyProduct(id, name, quantity, price);
            } else {
                // This case should never occur due to controller-level validation
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

            logStatus("Added", "product " + product.getID(), true, null);
        } catch (SQLException ex) {
            logStatus("Add", "product", false, ex.getMessage());
        }
        return product;
    }

    @Override
    public void removeProduct(String id) {
        try {
            String sql = "DELETE FROM " + TABLE_PRODUCTS + " WHERE " + COL_PRODUCT_ID + " = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();

            logStatus("Removed", "product " + id, true, null);
        } catch (SQLException ex) {
            logStatus("Remove", "product " + id, false, ex.getMessage());
        }
    }

    @Override
    public void updateQuantity(String id, int newQuantity) {
        try {
            String sql = "UPDATE " + TABLE_PRODUCTS + " SET " + COL_QUANTITY + " = ? WHERE " + COL_PRODUCT_ID + " = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, newQuantity);
            ps.setString(2, id);
            ps.executeUpdate();
            ps.close();

            logStatus("Updated", "quantity for product " + id, true, null);
        } catch (SQLException ex) {
            logStatus("Update", "quantity for product " + id, false, ex.getMessage());
        }
    }

    @Override
    public Product getProduct(String id) {
        Product product = null;
        try {
            String sql = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_PRODUCT_ID + " = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                product = ProductFactory.createFromResultSet(rs);
            }
            ps.close();
            rs.close();

            if (product != null) {
                logStatus("Fetched", "product " + id, true, null);
            } else {
                logStatus("Fetch", "product " + id, false, "Product not found.");
            }
        } catch (SQLException ex) {
            logStatus("Fetch", "product " + id, false, ex.getMessage());
        }
        return product;
    }

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

            logStatus("Fetched", "all products", true, null);
        } catch (SQLException ex) {
            logStatus("Fetch", "all products", false, ex.getMessage());
        }
        return products;
    }

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

            logStatus("Fetched", "low stock products (threshold: " + threshold + ")", true, null);
        } catch (SQLException ex) {
            logStatus("Fetch", "low stock products", false, ex.getMessage());
        }
        return products;
    }

    private String generateNextId() {
        try {
            String sql = "SELECT MAX(" + COL_PRODUCT_ID + ") AS MAX_ID FROM " + TABLE_PRODUCTS;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            int maxID = 0;
            if (rs != null && rs.next()) {
                String lastID = rs.getString("MAX_ID");
                if (lastID != null) {
                    maxID = Integer.parseInt(lastID.substring(1));
                }
            }

            ps.close();
            return String.format("P%03d", maxID + 1);
        } catch (SQLException ex) {
            logStatus("Generate", "new product ID", false, ex.getMessage());
            return "P_ERR"; // fallback
        }
    }
}
