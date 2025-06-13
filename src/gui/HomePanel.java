/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import static utils.ThemeManager.*;

/**
 * A GUI panel for displaying the home screen of the Inventory System.
 *
 * Shows a welcome message and a brief instruction for navigating the
 * application.
 */
public class HomePanel extends JPanel {

    /**
     * Constructs the HomePanel and initialises its layout and UI components.
     * Adds a central panel with styled welcome and instruction labels.
     */
    public HomePanel() {
        setLayout(new BorderLayout());
        stylePanel(this);

        JLabel welcomeLabel = new JLabel("Welcome to Inventory System!", SwingConstants.CENTER);
        styleTitleLabel(welcomeLabel);

        JLabel subLabel = new JLabel("Please choose an option from the menu above", SwingConstants.CENTER);
        styleSubLabel(subLabel);

        JPanel centPanel = new JPanel(new GridLayout(2, 1));
        stylePanel(centPanel);
        centPanel.add(welcomeLabel);
        centPanel.add(subLabel);

        add(centPanel, BorderLayout.CENTER);
    }
}
