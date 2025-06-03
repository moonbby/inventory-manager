/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author lifeo
 */
public class DatabaseManager {

    private static final String URL = "jdbc:derby:inventoryDB;create=true";
    private static final int MAX_RETRIES = 3;
    private static final DatabaseManager instance = new DatabaseManager();
    private Connection conn;

    static {
        System.setProperty("derby.system.home", "database");
    }

    private DatabaseManager() {
        establishConnection();
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

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

    public void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {

            }
        }
    }
}
