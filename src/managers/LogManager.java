/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import static utils.DBConstants.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Handles logging of user activity and system events for audit and
 * traceability.
 *
 * Supports adding timestamped entries, retrieving logs for viewing, and
 * resetting the log file on application start.
 */
public class LogManager {

    Connection conn = DatabaseManager.getInstance().getConnection();

    // Adds a new log entry with a timestamp to the log file.
    public void log(String action, String target, boolean success, String errorMessage) {
        try {
            StringBuilder message = new StringBuilder();

            if (success) {
                message.append("Successfully ").append(action.toLowerCase()).append(" ").append(target);
            } else {
                message.append("Failed to ").append(action.toLowerCase()).append(" ").append(target);
                if (errorMessage != null && !errorMessage.isBlank()) {
                    message.append(": ").append(errorMessage);
                }
            }

            String sql = "INSERT INTO " + TABLE_LOGS + " (ACTION) VALUES (?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, message.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            // Avoid recursive loop - log() inside log() causes infinite loop on failure
            System.err.println("Failed to insert log: " + ex.getMessage());;
        }
    }

    public void logRaw(String message) {
        try {
            String sql = "INSERT INTO " + TABLE_LOGS + " (ACTION) VALUES (?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, message);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            // Avoid recursive loop - log() inside log() causes infinite loop on failure
            System.err.println("Failed to insert log: " + ex.getMessage());;
        }
    }

    // Returns all log entries from the log file as a list of strings.
    public List<Object[]> getLogs() {
        List<Object[]> logs = new ArrayList<>();

        try {
            String sql = "SELECT * FROM " + TABLE_LOGS + " ORDER BY " + COL_TIMESTAMP + " ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                String action = rs.getString(2);
                Timestamp timestamp = rs.getTimestamp(3);

                logs.add(new Object[]{id, action, timestamp});
            }
            ps.close();
            rs.close();

            log("Fetched", "all log entries", true, null);
        } catch (SQLException ex) {
            log("Fetch", "logs", false, ex.getMessage());
        }

        return logs;
    }

    public void resetLogs() {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM " + TABLE_LOGS);
            statement.close();
            log("Cleared", "logs", true, null);
        } catch (SQLException ex) {
            log("Clear", "logs", false, ex.getMessage());
        }
    }

}
