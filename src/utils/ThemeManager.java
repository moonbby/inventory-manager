/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import static utils.ThemeColours.*;

/**
 * Provides theming utilities to ensure consistent visual styling across all UI
 * components.
 *
 * Applies colour schemes and font styles from ThemeColours to buttons, labels,
 * panels, and option panes. Designed to centralise and simplify GUI appearance
 * management.
 */
public class ThemeManager {

    /**
     * Applies global theme settings to option panes and buttons.
     */
    public static void applyGlobalOptionPaneTheme() {
        UIManager.put("OptionPane.background", PASTEL_PINK);
        UIManager.put("Panel.background", PASTEL_PINK);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("SansSerif", Font.PLAIN, 13));
        UIManager.put("Button.background", BUTTON_PINK);
        UIManager.put("Button.foreground", COBALT_BLUE);
    }

    /**
     * Applies pastel background styling to a JPanel.
     *
     * @param panel the panel to style
     */
    public static void stylePanel(JPanel panel) {
        panel.setBackground(PASTEL_PINK);
    }

    /**
     * Styles a JLabel as a main title label. Applies bold font and cobalt
     * colour for header emphasis.
     *
     * @param label the label to style
     */
    public static void styleTitleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 35));
        label.setForeground(COBALT_BLUE);
    }

    /**
     * Styles a JLabel as a subtitle or hint label. Uses lighter font and colour
     * to show secondary information.
     *
     * @param label the label to style
     */
    public static void styleSubLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(LIGHTER_COBALT);
    }

    /**
     * Applies consistent styling to a JButton.
     *
     * @param button the button to style
     */
    public static void styleButton(JButton button) {
        button.setBackground(BUTTON_PINK);
        button.setForeground(COBALT_BLUE);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(5, 15, 5, 15));
    }

    /**
     * Creates a titled border for sectioning UI panels.
     *
     * @param title the section title
     * @return a customised TitledBorder instance
     */
    public static TitledBorder createSectionBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 13),
                COBALT_BLUE
        );
    }
}
