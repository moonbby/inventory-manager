/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javax.swing.UIManager;
import java.awt.Font;
import static utils.ThemeColours.*;

/**
 *
 * @author lifeo
 */
public class ThemeInitialiser {
    
    public static void applyGlobalOptionPaneTheme() {
        UIManager.put("OptionPane.background", PASTEL_PINK);
        UIManager.put("Panel.background", PASTEL_PINK);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("SansSerif", Font.PLAIN, 13));
        UIManager.put("Button.background", BUTTON_PINK);
        UIManager.put("Button.foreground", COBALT_BLUE);
    }
}
