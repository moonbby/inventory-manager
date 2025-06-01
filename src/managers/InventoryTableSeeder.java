/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import static utils.DBConstants.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author lifeo
 */
public class InventoryTableSeeder {

    private final Connection conn = DatabaseManager.getInstance().getConnection();
    private Statement statement;

    public void seedAllTables() {
        try {
            conn.setAutoCommit(false);
            createProductsTableWithSeedData();
            createBackupProductsTable();
            createLogsTable();
            conn.commit();
            System.out.println("All tables created and seeded successfully.");
        } catch (SQLException ex) {
            try {
                conn.rollback();
                System.err.println("Seeding failed. Rolled back changes.");
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.err.println("SQLException during seeding: " + ex.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Failed to reset auto-commit: " + e.getMessage());
            }
        }
    }
    
    public void createProductsTableWithSeedData() {
        try {
            if (!tableExists(TABLE_PRODUCTS)) {
                this.statement = conn.createStatement();
                this.statement.addBatch("CREATE TABLE " + TABLE_PRODUCTS + " (" + COL_PRODUCT_ID + " VARCHAR(10) PRIMARY KEY, "
                        + COL_NAME + " VARCHAR(50), " + COL_QUANTITY + " INT, "
                        + COL_PRICE + " DOUBLE, " + COL_TYPE + " VARCHAR(20))");

                this.statement.addBatch("INSERT INTO " + TABLE_PRODUCTS + " VALUES "
                        + "('P001', 'Jeans', 51, 120.5, 'Clothing'),\n"
                        + "('P002', 'Pajamas', 7, 78.89, 'Clothing'),\n"
                        + "('P003', 'Chess', 72, 34.66, 'Toy'),\n"
                        + "('P004', 'Console', 11, 89.99, 'Toy'),\n"
                        + "('P005', 'Jacket', 5, 89.0, 'Clothing'),\n"
                        + "('P006', 'Dress', 23, 56.99, 'Clothing'),\n"
                        + "('P007', 'T-Shirt', 36, 50.99, 'Clothing'),\n"
                        + "('P008', 'Teddy Bear', 100, 21.99, 'Toy'),\n"
                        + "('P009', 'Slime', 99, 13.5, 'Toy')");

                this.statement.executeBatch();
                System.out.println(TABLE_PRODUCTS + " table created and seeded successfully.");
            } else {
                System.out.println(TABLE_PRODUCTS + " table already exists. No action taken.");
            }
        } catch (SQLException ex) {
            System.err.println("SQLException during seeding: " + ex.getMessage());
        }
    }

    public void createBackupProductsTable() {
        try {
            if (!tableExists(TABLE_BACKUP)) {
                this.statement = conn.createStatement();
                this.statement.executeUpdate("CREATE TABLE " + TABLE_BACKUP + " ("
                        + COL_PRODUCT_ID + " VARCHAR(10) PRIMARY KEY, "
                        + COL_NAME + " VARCHAR(50), "
                        + COL_QUANTITY + " INT, "
                        + COL_PRICE + " DOUBLE, "
                        + COL_TYPE + " VARCHAR(20))");

                System.out.println(TABLE_BACKUP + " table created successfully.");
            } else {
                System.out.println(TABLE_BACKUP + " table already exists. No action taken.");
            }
        } catch (SQLException ex) {
            System.err.println("SQLException during creating backup table: " + ex.getMessage());
        }
    }

    public void createLogsTable() {
        try {
            if (!tableExists(TABLE_LOGS)) {
                this.statement = conn.createStatement();

                this.statement.executeUpdate("CREATE TABLE " + TABLE_LOGS + " ("
                        + COL_LOG_ID + " INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                        + COL_ACTION + " VARCHAR(255), "
                        + COL_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

                System.out.println(TABLE_LOGS + " table created successfully.");
            } else {
                System.out.println(TABLE_LOGS + " table already exists. No action taken.");
            }
        } catch (SQLException ex) {
            System.err.println("SQLException during creating logs table: " + ex.getMessage());
        }
    }

    private boolean tableExists(String tableName) {
        try {
            DatabaseMetaData dbMeta = this.conn.getMetaData();
            ResultSet rs = dbMeta.getTables(null, null, tableName.toUpperCase(), new String[]{"TABLE"});
            boolean exists = rs.next();
            rs.close();
            return exists;
        } catch (SQLException ex) {
            System.err.println("SQLException checking table: " + ex.getMessage());
            return false;
        }
    }

    public void closeConnection() {
        DatabaseManager.getInstance().closeConnections();
    }
}
