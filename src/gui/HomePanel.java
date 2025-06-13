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
 *
 * @author lifeo
 */
public class HomePanel extends JPanel {

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
