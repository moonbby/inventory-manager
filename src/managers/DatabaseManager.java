/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static utils.LoggerUtil.logStatus;

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
        if (conn == null) {
            logStatus("Reconnect", "database", false, "Connection is null. Attempting to re-establish...");
            establishConnection();
        }
        return conn;
    }

    public void establishConnection() {
        if (conn != null) {
            return;
        }

        int attempts = 0;
        while (attempts < MAX_RETRIES) {
            try {
                conn = DriverManager.getConnection(URL);
                logStatus("Connected", URL, true, null);
                break;
            } catch (SQLException ex) {
                attempts++;
                logStatus("Retry", "connection attempt " + attempts, false, ex.getMessage());
                if (attempts == MAX_RETRIES) {
                    logStatus("Connect", "database", false, "Could not establish DB connection after " + MAX_RETRIES + " attempts.");
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    logStatus("Retry", "connection", false, "Interrupted during wait.");
                    break;
                }
            }
        }
    }

    public void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
                logStatus("Closed", "database connection", true, null);
            } catch (SQLException ex) {
                logStatus("Close", "database connection", false, ex.getMessage());
            }
        }
    }
}
