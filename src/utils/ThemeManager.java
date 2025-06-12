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
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import static utils.ThemeColours.*;

/**
 *
 * @author lifeo
 */
public class ThemeManager {

    public static void stylePanel(JPanel panel) {
        panel.setBackground(PASTEL_PINK);
    }

    public static void styleTitleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 35));
        label.setForeground(COBALT_BLUE);
    }

    public static void styleSubLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(LIGHTER_COBALT);
    }

    public static void styleButton(JButton button) {
        button.setBackground(BUTTON_PINK);
        button.setForeground(COBALT_BLUE);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(5, 15, 5, 15));
    }

    public static void styleFormLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(COBALT_BLUE);
    }

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
