/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import interfaces.IInventoryManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import managers.InventoryManager;
import managers.LogManager;
import managers.ReportManager;

/**
 *
 * @author lifeo
 */
public class MainWindow extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private IInventoryManager inventoryManager = new InventoryManager();
    private LogManager logManager = new LogManager();
    private ReportManager reportManager = new ReportManager(inventoryManager, logManager);

    public MainWindow() {
        setTitle("Inventory Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel navPanel = new JPanel();
        JButton btnProducts = new JButton("Products");
        JButton btnUtilities = new JButton("Utilities");
        JButton btnReports = new JButton("Reports");
        JButton btnDeveloper = new JButton("Developer");
        navPanel.add(btnProducts);
        navPanel.add(btnUtilities);
        navPanel.add(btnReports);
        navPanel.add(btnDeveloper);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        cardPanel.add(new ProductPanel(inventoryManager, logManager), "PRODUCTS");
        cardPanel.add(new UtilityPanel(), "UTILITIES");
        cardPanel.add(new ReportPanel(reportManager, logManager), "REPORTS");
        cardPanel.add(new DeveloperPanel(), "DEVELOPER");

        setLayout(new BorderLayout());
        add(navPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
        
        btnProducts.addActionListener(e -> cardLayout.show(cardPanel, "PRODUCTS"));
        btnUtilities.addActionListener(e -> cardLayout.show(cardPanel, "UTILITIES"));
        btnReports.addActionListener(e -> cardLayout.show(cardPanel, "REPORTS"));
        btnDeveloper.addActionListener(e -> cardLayout.show(cardPanel, "DEVELOPER"));
    }
}
