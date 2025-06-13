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
 * Handles creation, seeding, and resetting of the database tables required by
 * the application (Products table, Backup table, Logs table).
 */
public class InventoryTableSeeder {

    private final Connection conn = DatabaseManager.getInstance().getConnection();
    private final LogManager logManager = new LogManager();

    /**
     * Initialises all required database tables and populates initial product
     * data.
     */
    public void initialiseAllTables() {
        try {
            conn.setAutoCommit(false);
            createAndSeedProductsTable();
            createBackupProductsTable();
            createLogsTable();
            conn.commit();
            logManager.log("Initialised", "all tables", true, null);
        } catch (SQLException ex) {
            logManager.log("Initialise", "all tables", false, ex.getMessage());
            try {
                conn.rollback();
                logManager.log("Rollback", "changes", true, null);
            } catch (SQLException rollbackEx) {
                logManager.log("Rollback", "changes", false, rollbackEx.getMessage());
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                logManager.log("Reset", "auto-commit", false, e.getMessage());
            }
        }
    }

    /**
     * Creates the main products table if it does not exist. On success, seeds
     * the table with default product entries.
     */
    public void createAndSeedProductsTable() {
        if (!tableExists(TABLE_PRODUCTS)) {
            Statement statement = null;
            try {
                statement = conn.createStatement();
                statement.executeUpdate("CREATE TABLE " + TABLE_PRODUCTS + " ("
                        + COL_PRODUCT_ID + " VARCHAR(10) PRIMARY KEY, "
                        + COL_NAME + " VARCHAR(50), "
                        + COL_QUANTITY + " INT, "
                        + COL_PRICE + " DOUBLE, "
                        + COL_TYPE + " VARCHAR(20))");
                logManager.log("Created", "products table", true, null);
                seedProductsTable();
            } catch (SQLException ex) {
                logManager.log("Create", "products table", false, ex.getMessage());
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ignore) {
                    }
                }
            }
        } else {
            logManager.log("Check", "products table", true, "Table already exists. Skipped creation.");
        }
    }

    /**
     * Inserts predefined products into the products table.
     */
    public void seedProductsTable() {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.addBatch("INSERT INTO " + TABLE_PRODUCTS + " VALUES "
                    + "('P001', 'Jeans', 51, 120.5, 'Clothing'),"
                    + "('P002', 'Pajamas', 7, 78.89, 'Clothing'),"
                    + "('P003', 'Chess', 72, 34.66, 'Toy'),"
                    + "('P004', 'Console', 11, 89.99, 'Toy'),"
                    + "('P005', 'Jacket', 5, 89.0, 'Clothing'),"
                    + "('P006', 'Dress', 23, 56.99, 'Clothing'),"
                    + "('P007', 'T-Shirt', 36, 50.99, 'Clothing'),"
                    + "('P008', 'Teddy Bear', 100, 21.99, 'Toy'),"
                    + "('P009', 'Slime', 99, 13.5, 'Toy')");
            statement.executeBatch();
            logManager.log("Seeded", "products table", true, null);
        } catch (SQLException ex) {
            logManager.log("Seed", "products table", false, ex.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    /**
     * Creates the backup table if it does not exist.
     */
    public void createBackupProductsTable() {
        if (!tableExists(TABLE_BACKUP)) {
            Statement statement = null;
            try {
                statement = conn.createStatement();
                statement.executeUpdate("CREATE TABLE " + TABLE_BACKUP + " ("
                        + COL_PRODUCT_ID + " VARCHAR(10) PRIMARY KEY, "
                        + COL_NAME + " VARCHAR(50), "
                        + COL_QUANTITY + " INT, "
                        + COL_PRICE + " DOUBLE, "
                        + COL_TYPE + " VARCHAR(20))");
                logManager.log("Created", "backup table", true, null);
            } catch (SQLException ex) {
                logManager.log("Create", "backup table", false, ex.getMessage());
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ignore) {
                    }
                }
            }
        } else {
            logManager.log("Check", "backup table", true, "Table already exists. Skipped creation.");
        }
    }

    /**
     * Creates the logs table if it does not exist.
     */
    public void createLogsTable() {
        if (!tableExists(TABLE_LOGS)) {
            Statement statement = null;
            try {
                statement = conn.createStatement();
                statement.executeUpdate("CREATE TABLE " + TABLE_LOGS + " ("
                        + COL_LOG_ID + " INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                        + COL_ACTION + " VARCHAR(255), "
                        + COL_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
                logManager.log("Created", "logs table", true, null);
            } catch (SQLException ex) {
                logManager.log("Create", "logs table", false, ex.getMessage());
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ignore) {
                    }
                }
            }
        } else {
            logManager.log("Check", "logs table", true, "Table already exists. Skipped creation.");
        }
    }

    /**
     * Clears all entries from the products table and repopulates it with
     * default data.
     */
    public void resetProductsTable() {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM " + TABLE_PRODUCTS);
            logManager.log("Reset", "products table", true, null);
            seedProductsTable();
        } catch (SQLException ex) {
            logManager.log("Reset", "products table", false, ex.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    /**
     * Clears all entries from the logs table and resets its primary key
     * sequence.
     */
    public void resetLogsTable() {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM " + TABLE_LOGS);
            statement.executeUpdate("ALTER TABLE " + TABLE_LOGS + " ALTER COLUMN " + COL_LOG_ID + " RESTART WITH 1");
            logManager.log("Reset", "logs table", true, null);
        } catch (SQLException ex) {
            logManager.log("Reset", "logs table", false, ex.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    /**
     * Clears all entries from the backup table.
     */
    public void resetBackupTable() {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM " + TABLE_BACKUP);
            logManager.log("Reset", "backup table", true, null);
        } catch (SQLException ex) {
            logManager.log("Reset", "backup table", false, ex.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    /**
     * Closes the database connection managed by DatabaseManager.
     */
    public void closeConnection() {
        DatabaseManager.getInstance().closeConnections();
    }

    /**
     * Checks whether a given table exists in the connected database.
     *
     * @param tableName the name of the table to check
     * @return true if the table exists; false otherwise
     */
    private boolean tableExists(String tableName) {
        try {
            DatabaseMetaData dbMeta = this.conn.getMetaData();
            ResultSet rs = dbMeta.getTables(null, null, tableName.toUpperCase(), new String[]{"TABLE"});
            boolean exists = rs.next();
            rs.close();
            return exists;
        } catch (SQLException ex) {
            logManager.log("Check", "if table " + tableName + " exists", false, ex.getMessage());
            return false;
        }
    }
}
