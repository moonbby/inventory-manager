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
    private final LogManager logManager = new LogManager();

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
                logManager.log("Created", TABLE_PRODUCTS, true, null);
                seedProductsTable();
            } catch (SQLException ex) {
                logManager.log("Create", TABLE_PRODUCTS, false, ex.getMessage());
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ignore) {
                    }
                }
            }
        } else {
            logManager.log("Check", TABLE_PRODUCTS, true, "Table already exists. Skipped creation.");
        }
    }

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
            logManager.log("Seeded", TABLE_PRODUCTS, true, null);
        } catch (SQLException ex) {
            logManager.log("Seed", TABLE_PRODUCTS, false, ex.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

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
                logManager.log("Created", TABLE_BACKUP, true, null);
            } catch (SQLException ex) {
                logManager.log("Create", TABLE_BACKUP, false, ex.getMessage());
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ignore) {
                    }
                }
            }
        } else {
            logManager.log("Check", TABLE_BACKUP, true, "Table already exists. Skipped creation.");
        }
    }

    public void createLogsTable() {
        if (!tableExists(TABLE_LOGS)) {
            Statement statement = null;
            try {
                statement = conn.createStatement();
                statement.executeUpdate("CREATE TABLE " + TABLE_LOGS + " ("
                        + COL_LOG_ID + " INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                        + COL_ACTION + " VARCHAR(255), "
                        + COL_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
                logManager.log("Created", TABLE_LOGS, true, null);
            } catch (SQLException ex) {
                logManager.log("Create", TABLE_LOGS, false, ex.getMessage());
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ignore) {
                    }
                }
            }
        } else {
            logManager.log("Check", TABLE_LOGS, true, "Table already exists. Skipped creation.");
        }
    }

    public void resetProductsTable() {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM " + TABLE_PRODUCTS);
            logManager.log("Reset", TABLE_PRODUCTS, true, null);
            seedProductsTable();
        } catch (SQLException ex) {
            logManager.log("Reset", TABLE_PRODUCTS, false, ex.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    public void resetLogsTable() {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM " + TABLE_LOGS);
            logManager.log("Reset", TABLE_LOGS, true, null);
        } catch (SQLException ex) {
            logManager.log("Reset", TABLE_LOGS, false, ex.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    public void resetBackupTable() {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM " + TABLE_BACKUP);
            logManager.log("Reset", TABLE_BACKUP, true, null);
        } catch (SQLException ex) {
            logManager.log("Reset", TABLE_BACKUP, false, ex.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    public void closeConnection() {
        DatabaseManager.getInstance().closeConnections();
    }

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
