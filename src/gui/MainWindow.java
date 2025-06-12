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
    private final IInventoryManager inventoryManager = new InventoryManager();
    private final LogManager logManager = new LogManager();
    private final BackupManager backupManager = new BackupManager(logManager);
    private final ReportManager reportManager = new ReportManager(inventoryManager, logManager);
    private final DeveloperManager devManager = new DeveloperManager();
    private HomePanel homePanel;
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
        
        JButton btnHome = new JButton("Home");
        JButton btnProducts = new JButton("Products");
        JButton btnReports = new JButton("Reports");
        JButton btnBackup = new JButton("Backup");
        JButton btnLog = new JButton("Logs");
        JButton btnDeveloper = new JButton("Developer");
        
        navPanel.add(btnHome);
        navPanel.add(btnProducts);
        navPanel.add(btnReports);
        navPanel.add(btnBackup);
        navPanel.add(btnLog);
        navPanel.add(btnDeveloper);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        homePanel = new HomePanel();
        productPanel = new ProductPanel(inventoryManager);
        reportPanel = new ReportPanel(reportManager);
        backupPanel = new BackupPanel(backupManager);
        logPanel = new LogPanel(logManager);
        developerPanel = new DeveloperPanel(devManager, productPanel, logPanel, backupPanel);

        cardPanel.add(homePanel, "HOME");
        cardPanel.add(productPanel, "PRODUCTS");
        cardPanel.add(reportPanel, "REPORTS");
        cardPanel.add(backupPanel, "BACKUP");
        cardPanel.add(logPanel, "LOGS");
        cardPanel.add(developerPanel, "DEVELOPER");

        setLayout(new BorderLayout());
        add(navPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        btnHome.addActionListener(e -> cardLayout.show(cardPanel, "HOME"));
        btnProducts.addActionListener(e -> cardLayout.show(cardPanel, "PRODUCTS"));
        btnReports.addActionListener(e -> cardLayout.show(cardPanel, "REPORTS"));
        btnBackup.addActionListener(e -> cardLayout.show(cardPanel, "BACKUP"));
        btnLog.addActionListener(e -> cardLayout.show(cardPanel, "LOGS"));
        btnDeveloper.addActionListener(e -> cardLayout.show(cardPanel, "DEVELOPER"));
    }
}
