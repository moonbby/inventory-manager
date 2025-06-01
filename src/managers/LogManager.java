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
import static utils.LoggerUtil.logStatus;

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
    public void log(String message) {
        try {
            String sql = "INSERT INTO " + TABLE_LOGS + " (ACTION) VALUES (?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, message);
            ps.executeUpdate();
            ps.close();
            logStatus("Inserted", "log entry", true, null);
        } catch (SQLException ex) {
            logStatus("Insert", "log entry", false, ex.getMessage());
        }
    }

    // Returns all log entries from the log file as a list of strings.
    public List<String> getLogs() {
        List<String> logs = new ArrayList<String>();

        try {
            String sql = "SELECT * FROM " + TABLE_LOGS + " ORDER BY " + COL_TIMESTAMP + " ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                String action = rs.getString(2);
                Timestamp timestamp = rs.getTimestamp(3);

                logs.add("ID: " + id + " | Action: " + action + " | Time: " + timestamp);
            }
            ps.close();
            rs.close();
            logStatus("Fetched", "all log entries", true, null);
        } catch (SQLException ex) {
            logStatus("Fetch", "logs", false, ex.getMessage());
        }
        
        return logs;
    }

    public void resetLogs() {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM " + TABLE_LOGS);
            statement.close();
            logStatus("Cleared", "logs", true, null);
        } catch (SQLException ex) {
            logStatus("Clear", "logs", false, ex.getMessage());
        }
    }

}
