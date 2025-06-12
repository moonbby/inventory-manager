/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author lifeo
 */
public class HomePanel extends JPanel {

    public HomePanel() {
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Inventory System!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 35));

        JLabel subLabel = new JLabel("Please choose an option from the menu above", SwingConstants.CENTER);
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JPanel centPanel = new JPanel(new GridLayout(2, 1));
        centPanel.add(welcomeLabel);
        centPanel.add(subLabel);
        add(centPanel, BorderLayout.CENTER);
    }
}
