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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.FilePaths;

/**
 * Handles inventory backup operations to support data recovery.
 *
 * Provides functionality to duplicate the current inventory and retrieve the
 * latest backup. Ensures inventory data is preserved across sessions and
 * protected against loss or corruption.
 */
public class BackupManager {

    /**
     * Creates a backup of the current inventory by copying the contents of the
     * main inventory file to a separate backup file.
     *
     * Replaces any existing backup and logs the operation.
     */
    public static void backupInventory() {
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(FilePaths.BACKUP_FILE_PATH));
            BufferedReader br = new BufferedReader(new FileReader(FilePaths.INVENTORY_FILE_PATH));
            String line = null;

            while ((line = br.readLine()) != null) {
                pw.println(line);
            }

            pw.close();
            br.close();

            LogManager.log("Created backup of inventory.");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Retrieves the contents of the latest inventory backup file.
     *
     * Returns each line as a string in a list. Access to the backup is logged.
     *
     * @return a list of backup entries
     */
    public static ArrayList<String> getBackup() {
        ArrayList<String> backup = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(FilePaths.BACKUP_FILE_PATH));
            String line = null;

            while ((line = br.readLine()) != null) {
                backup.add(line);
            }

            br.close();
            LogManager.log("Viewed backup of inventory.");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return backup;
    }
}
