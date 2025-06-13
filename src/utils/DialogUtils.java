/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * Utility class for displaying dialog messages in the GUI.
 *
 * Provides methods for showing information, error, and success messages,
 * as well as prompting user input from the user interface.
 */
public class DialogUtils {
    
    /**
     * Displays an informational dialog with the given message.
     *
     * @param parent the component over which the dialog appears
     * @param message the message to display
     */
    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays an error dialog with the given message.
     *
     * @param parent the component over which the dialog appears
     * @param message the error message to display
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays a success dialog with the given message.
     *
     * @param parent the component over which the dialog appears
     * @param message the success message to display
     */
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Prompts the user for input using a dialog box.
     *
     * @param parent the component over which the dialog appears
     * @param message the prompt message to display
     * @return the user input as a string; null if cancelled
     */
    public static String promptInput(Component parent, String message) {
        return JOptionPane.showInputDialog(parent, message);
    }
}
