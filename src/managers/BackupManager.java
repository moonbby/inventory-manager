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
 * Manages backup operations for inventory data within the database.
 *
 * Facilitates inventory backup creation and entry retrieval. Used by the GUI to
 * support data recovery and provide visibility into previous inventory states.
 */
public class BackupManager {

    private final Connection conn = DatabaseManager.getInstance().getConnection();
    private final LogManager logManager;

    /**
     * Constructs a BackupManager with the specified LogManager.
     *
     * @param logManager the log manager used to record backup activity
     */
    public BackupManager(LogManager logManager) {
        this.logManager = logManager;
    }

    /**
     * Creates a database-level backup by copying all products from the main
     * inventory table into the backup table.
     *
     * Clears any existing backup entries before inserting the current state.
     * Logs the operation outcome.
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
     * Retrieves all products currently stored in the backup table. Logs the
     * retrieval operation and returns a list of product objects.
     *
     * @return a list of products from the backup table; empty if none found
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
