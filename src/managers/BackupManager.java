/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import static utils.DBConstants.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.ClothingProduct;
import models.Product;
import models.ToyProduct;

/**
 * Handles inventory backup operations to support data recovery.
 *
 * Provides functionality to duplicate the current inventory and retrieve the
 * latest backup. Ensures inventory data is preserved across sessions and
 * protected against loss or corruption.
 */
public class BackupManager {

    private final Connection conn = DatabaseManager.getInstance().getConnection();

    /**
     * Creates a backup of the current inventory by copying the contents of the
     * main inventory file to a separate backup file.
     *
     * Replaces any existing backup and logs the operation.
     */
    public void backupInventory() {
        try {
            ProductDAO productDAO = new ProductDAO();
            List<Product> products = productDAO.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("No products to back up.");
                return;
            }

            conn.createStatement().executeUpdate("DELETE FROM " + TABLE_BACKUP);

            String sql = "INSERT INTO " + TABLE_BACKUP + " (" + COL_PRODUCT_ID + ", " + COL_NAME + ", "
                    + COL_QUANTITY + ", " + COL_PRICE + ", " + COL_TYPE + ") VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            for (Product p : products) {
                ps.setString(1, p.getID());
                ps.setString(2, p.getName() != null ? p.getName() : "Unnamed");
                ps.setInt(3, p.getQuantity());
                ps.setDouble(4, p.getPrice());
                ps.setString(5, p.getProductType() != null ? p.getProductType() : "Unknown");
                ps.addBatch();
            }

            ps.executeBatch();
            ps.close();

            System.out.println("Inventory successfully backed up to " + TABLE_BACKUP + " table.");
        } catch (SQLException ex) {
            System.err.println("SQLException during backup: " + ex.getMessage());
        }
    }

    /**
     * Retrieves the contents of the latest inventory backup file.
     *
     * Returns each line as a string in a list. Access to the backup is logged.
     *
     * @return a list of backup entries
     */
    public List<Product> getBackup() {
        List<Product> products = new ArrayList<>();
        
        try {
            String sql = "SELECT * FROM " + TABLE_BACKUP;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString(2);
                int quantity = rs.getInt(3);
                double price = rs.getDouble(4);
                String type = rs.getString(5);

                if (type.equalsIgnoreCase("Clothing")) {
                    products.add(new ClothingProduct(id, name, quantity, price));
                } else if (type.equalsIgnoreCase("Toy")) {
                    products.add(new ToyProduct(id, name, quantity, price));
                }
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            System.err.println("SQLException during getting backup: " + ex.getMessage());
        }
        
        return products;
    }
}
