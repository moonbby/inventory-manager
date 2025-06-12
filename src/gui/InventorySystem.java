/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import javax.swing.SwingUtilities;
import utils.ThemeInitialiser;
import static utils.ThemeInitialiser.*;

/**
 *
 * @author lifeo
 */
public class InventorySystem {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ThemeInitialiser.applyGlobalOptionPaneTheme();
            new MainWindow().setVisible(true);
        });
    }
}
