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
import models.Product;
import utils.ProductFactory;

/**
 * Handles inventory backup operations to support data recovery.
 *
 * Provides functionality to duplicate the current inventory and retrieve the
 * latest backup. Ensures inventory data is preserved across sessions and
 * protected against loss or corruption.
 */
public class BackupManager {

    private final Connection conn = DatabaseManager.getInstance().getConnection();
    private final LogManager logManager;
    
    public BackupManager(LogManager logManager) {
        this.logManager = logManager;
    }

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
                logManager.logRaw("No products found to back up.");
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

            logManager.log("Backed up", "inventory to backup table", true, null);
        } catch (SQLException ex) {
            logManager.log("Backup", "inventory", false, ex.getMessage());
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
                Product product = ProductFactory.createFromResultSet(rs);
                products.add(product);
            }
            
            ps.close();
            rs.close();
            
            logManager.log("Fetched", "backup products", true, null);
        } catch (SQLException ex) {
            logManager.log("Fetch", "backup products", false, ex.getMessage());
        }
        
        return products;
    }
}
