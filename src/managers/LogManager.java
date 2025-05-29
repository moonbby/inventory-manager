/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.FilePaths;

/**
 * Handles logging of user activity and system events for audit and
 * traceability.
 *
 * Supports adding timestamped entries, retrieving logs for viewing, and
 * resetting the log file on application start.
 */
public class LogManager {

    // Adds a new log entry with a timestamp to the log file.
    public static void log(String message) {
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(FilePaths.LOG_FILE_PATH, true));

            String time = LocalDateTime.now().toString();
            pw.println(time + " - " + message);
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Returns all log entries from the log file as a list of strings.
    public static ArrayList<String> getLogs() {
        ArrayList<String> logs = new ArrayList<String>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(FilePaths.LOG_FILE_PATH));
            String line = null;

            while ((line = br.readLine()) != null) {
                logs.add(line);
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return logs;
    }

    /**
     * Clears the log file to start a fresh session.
     *
     * Called at application start.
     */
    public static void initialize() {
        try {
            PrintWriter pw = new PrintWriter(FilePaths.LOG_FILE_PATH);
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LogManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
