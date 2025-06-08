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
import managers.BackupManager;
import managers.DeveloperManager;
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
    private BackupManager backupManager = new BackupManager(logManager);
    private ReportManager reportManager = new ReportManager(inventoryManager, logManager);
    private DeveloperManager devManager = new DeveloperManager();
    private ProductPanel productPanel;
    private ReportPanel reportPanel;
    private BackupPanel backupPanel;
    private LogPanel logPanel;
    private DeveloperPanel developerPanel;

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
        JButton btnReports = new JButton("Reports");
        JButton btnBackup = new JButton("Backup");
        JButton btnLog = new JButton("Logs");
        JButton btnDeveloper = new JButton("Developer");
        navPanel.add(btnProducts);
        navPanel.add(btnReports);
        navPanel.add(btnBackup);
        navPanel.add(btnLog);
        navPanel.add(btnDeveloper);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        productPanel = new ProductPanel(inventoryManager, logManager);
        reportPanel = new ReportPanel(reportManager, logManager);
        backupPanel = new BackupPanel(backupManager, logManager);
        logPanel = new LogPanel(logManager);
        developerPanel = new DeveloperPanel(devManager, logManager, productPanel, logPanel, backupPanel);

        cardPanel.add(productPanel, "PRODUCTS");
        cardPanel.add(reportPanel, "REPORTS");
        cardPanel.add(backupPanel, "BACKUP");
        cardPanel.add(logPanel, "LOGS");
        cardPanel.add(developerPanel, "DEVELOPER");

        setLayout(new BorderLayout());
        add(navPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        btnProducts.addActionListener(e -> cardLayout.show(cardPanel, "PRODUCTS"));
        btnReports.addActionListener(e -> cardLayout.show(cardPanel, "REPORTS"));
        btnBackup.addActionListener(e -> cardLayout.show(cardPanel, "BACKUP"));
        btnLog.addActionListener(e -> cardLayout.show(cardPanel, "LOGS"));
        btnDeveloper.addActionListener(e -> cardLayout.show(cardPanel, "DEVELOPER"));
    }
}
