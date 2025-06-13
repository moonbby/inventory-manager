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
import java.text.SimpleDateFormat;

/**
 * Manages system and user event logging for traceability and auditing.
 *
 * Logs activity to the database with timestamps, supports raw logging, and
 * retrieval of entries for GUI display.
 */
public class LogManager {

    Connection conn = DatabaseManager.getInstance().getConnection();

    /**
     * Logs a formatted event message with timestamp and outcome. Automatically
     * prefixes the log entry with "Successfully" or "Failed" based on outcome.
     * If an error message is provided, it is appended to the log entry.
     *
     * @param action the action performed (e.g., "added", "deleted")
     * @param target the object or operation target
     * @param success whether the action was successful
     * @param errorMessage optional error message to include if failed
     */
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

    /**
     * Logs a raw custom message directly to the database log table.
     *
     * @param message the message to log
     */
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

    /**
     * Retrieves all logs from the database ordered by timestamp.
     *
     * @return list of log entries, each as an Object array [id, action,
     * timestamp]
     */
    public List<Object[]> getLogs() {
        List<Object[]> logs = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            String sql = "SELECT * FROM " + TABLE_LOGS + " ORDER BY " + COL_TIMESTAMP + " ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                String action = rs.getString(2);
                Timestamp timestamp = rs.getTimestamp(3);
                String formattedTime = formatter.format(timestamp);

                logs.add(new Object[]{id, action, formattedTime});
            }
            ps.close();
            rs.close();

            log("Fetched", "all log entries", true, null);
        } catch (SQLException ex) {
            log("Fetch", "logs", false, ex.getMessage());
        }

        return logs;
    }
}
