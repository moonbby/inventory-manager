/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import interfaces.IInventoryManager;
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
 * Generates analytical reports based on current inventory data.
 *
 * Provides category-based summaries and identifies the most expensive product.
 * Outputs are printed to the console and logged for traceability.
 */
public class ReportManager {

    Connection conn = DatabaseManager.getInstance().getConnection();
    private final LogManager logManager;
    private final IInventoryManager inventoryManager;

    public ReportManager(IInventoryManager inventoryManager,
             LogManager logManager) {
        this.inventoryManager = inventoryManager;
        this.logManager = logManager;
    }

    /**
     * Prints a summary of product counts by category.
     */
    public List<String[]> getSummaryCounts() {
        List<String[]> rows = new ArrayList<>();
        int total = 0;

        try {
            String sql = "SELECT " + COL_TYPE + ", COUNT(*) AS count FROM " + TABLE_PRODUCTS + " GROUP BY " + COL_TYPE;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String type = rs.getString(COL_TYPE);
                int count = rs.getInt("count");
                rows.add(new String[]{type, String.valueOf(count)});
                total += count;
            }

            rs.close();
            ps.close();

            // Add total row at the bottom
            rows.add(new String[]{"Total", String.valueOf(total)});

            logManager.log("Viewed", "summary report", true, null);
        } catch (SQLException ex) {
            logManager.log("View", "summary report", false, ex.getMessage());
        }

        return rows;
    }

    /**
     * Displays the most expensive product in the inventory.
     *
     * Prints product details or a warning if the inventory is empty.
     */
    public Product getMostExpensiveProduct() {
        try {
            String sql = "SELECT * FROM " + TABLE_PRODUCTS + " ORDER BY " + COL_PRICE + " DESC FETCH FIRST 1 ROWS ONLY";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product product = ProductFactory.createFromResultSet(rs);
                rs.close();
                ps.close();
                logManager.log("Viewed", "most expensive product", true, null);
                return product;
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            logManager.log("View", "most expensive product", false, ex.getMessage());
        }
        return null;
    }

    /**
     * Prompts for a stock threshold and exports low-stock products to file.
     *
     * Filters inventory below the threshold and delegates writing to
     * FileManager.
     */
    public List<Product> exportLowStockMenu(int threshold) {
        List<Product> lowStockProducts = inventoryManager.getLowStockProducts(threshold);
        return lowStockProducts;
    }
}
