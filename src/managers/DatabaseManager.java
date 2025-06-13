/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the database connection for the inventory system.
 *
 * Establishes a connection to an embedded Derby database and provides shared
 * access across the application. Handles retry logic and graceful shutdown.
 */
public class DatabaseManager {

    private static final String URL = "jdbc:derby:inventoryDB;create=true";
    private static final int MAX_RETRIES = 3;
    private static final DatabaseManager instance = new DatabaseManager();
    private Connection conn;

    /**
     * Sets the database storage directory to "database" folder.
     */
    static {
        System.setProperty("derby.system.home", "database");
    }

    /**
     * Private constructor to enforce singleton pattern.
     */
    private DatabaseManager() {
        establishConnection();
    }

    /**
     * Returns the singleton instance of the DatabaseManager.
     *
     * @return the shared DatabaseManager instance
     */
    public static DatabaseManager getInstance() {
        return instance;
    }

    /**
     * Provides access to the current database connection. If the connection is
     * null or closed, it attempts to re-establish it.
     *
     * @return the active database connection
     */
    public Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                establishConnection();
            }
        } catch (SQLException e) {
            establishConnection();
        }
        return conn;
    }

    /**
     * Attempts to establish a new database connection with retry logic.
     *
     * @return true if the connection was successfully established; false
     * otherwise
     */
    public boolean establishConnection() {
        if (conn != null) {
            return true;
        }

        int attempts = 0;
        while (attempts < MAX_RETRIES) {
            try {
                conn = DriverManager.getConnection(URL);
                return true;
            } catch (SQLException ex) {
                attempts++;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        return false;
    }

    /**
     * Closes the database connection if it is open.
     */
    public void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {

            }
        }
    }
}
