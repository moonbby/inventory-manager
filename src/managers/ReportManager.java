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
import static utils.ProductTypes.*;

/**
 * Generates analytical reports based on current inventory data.
 *
 * Provides category-based summaries, identifies the most expensive product, and
 * lists low stock items
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
     * Retrieves a breakdown of product counts grouped by category. Adds a final
     * row with the total count.
     *
     * @return list of rows where each row contains [type, count]
     */
    public List<String[]> getSummaryCounts() {
        List<String[]> rows = new ArrayList<>();
        int total = 0;

        try {
            String sql = "SELECT " + COL_TYPE + ", COUNT(*) AS count FROM " + TABLE_PRODUCTS + " GROUP BY " + COL_TYPE;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String typeRaw = rs.getString(COL_TYPE);
                int count = rs.getInt("count");

                String type;
                if (typeRaw.equalsIgnoreCase(CLOTHING)) {
                    type = CLOTHING;
                } else if (typeRaw.equalsIgnoreCase(TOY)) {
                    type = TOY;
                } else {
                    type = "Other";
                }

                rows.add(new String[]{type, String.valueOf(count)});
                total += count;
            }

            rs.close();
            ps.close();

            rows.add(new String[]{"Total", String.valueOf(total)});

            logManager.log("Viewed", "summary report", true, null);
        } catch (SQLException ex) {
            logManager.log("View", "summary report", false, ex.getMessage());
        }

        return rows;
    }

    /**
     * Retrieves the most expensive product from the database..
     *
     * @return the highest-priced product; null if empty
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
     * Retrieves all products with quantity below the given threshold.
     *
     * @param threshold the exclusive upper limit for stock level
     * @return list of low-stock products
     */
    public List<Product> exportLowStockMenu(int threshold) {
        List<Product> lowStockProducts = inventoryManager.getLowStockProducts(threshold);
        return lowStockProducts;
    }
}
