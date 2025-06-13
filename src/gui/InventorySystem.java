/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import javax.swing.SwingUtilities;
import static utils.ThemeManager.applyGlobalOptionPaneTheme;

/**
 * Entry point for launching the Inventory Management System GUI.
 *
 * Applies global theme styling and opens the main application window.
 */
public class InventorySystem {

    /**
     * Main method that starts the Swing application on the Event Dispatch
     * Thread. Ensures GUI components are created and updated in a thread-safe
     * manner.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            applyGlobalOptionPaneTheme();
            new MainWindow().setVisible(true);
        });
    }
}
