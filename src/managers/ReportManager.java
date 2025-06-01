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

/**
 * Generates analytical reports based on current inventory data.
 *
 * Provides category-based summaries and identifies the most expensive product.
 * Outputs are printed to the console and logged for traceability.
 */
public class ReportManager {

    Connection conn = DatabaseManager.getInstance().getConnection();
    private final LogManager logManager = new LogManager();

    /**
     * Prints a summary of product counts by category.
     *
     * @param products the current list of inventory items
     */
    public void viewSummaryReport() {
        try {
            System.out.println("\n=== Inventory Summary Report ===");

            String categoryCountSQL = "SELECT " + COL_TYPE + ", COUNT(*) AS count FROM "
                    + TABLE_PRODUCTS + " GROUP BY " + COL_TYPE;
            PreparedStatement ps = conn.prepareStatement(categoryCountSQL);
            ResultSet rs = ps.executeQuery();

            int total = 0;
            while (rs.next()) {
                String type = rs.getString(COL_TYPE);
                int count = rs.getInt("count");
                System.out.println(type + " products: " + count);
                total += count;
            }
            System.out.println("Total products: " + total);

            rs.close();
            ps.close();

            logManager.log("Viewed summary report.");
        } catch (SQLException ex) {
            System.err.println("SQLException during viewing summary report: " + ex.getMessage());
        }
    }

    /**
     * Displays the most expensive product in the inventory.
     *
     * Prints product details or a warning if the inventory is empty.
     *
     * @param products the current list of inventory items
     */
    public void viewMostExpensiveProduct() {
        try {
            String sql = "SELECT * FROM " + TABLE_PRODUCTS + " ORDER BY "
                    + COL_PRICE + " DESC FETCH FIRST 1 ROWS ONLY";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\n=== Most Expensive Product ===");
                System.out.println("Type: " + rs.getString(COL_TYPE));
                System.out.println("ID: " + rs.getString(COL_PRODUCT_ID));
                System.out.println("Name: " + rs.getString(COL_NAME));
                System.out.println("Price: $" + rs.getDouble(COL_PRICE));
                System.out.println("Quantity: " + rs.getInt(COL_QUANTITY));

                logManager.log("Viewed most expensive product.");
            } else {
                System.out.println("Inventory is empty.");
            }

            rs.close();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("SQLException during viewing most expensive product: " + ex.getMessage());
        }
    }
}
