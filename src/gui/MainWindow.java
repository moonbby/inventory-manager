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
import static utils.ThemeManager.*;

/**
 * The main window frame for the Inventory Management System GUI.
 *
 * Provides a navigational interface using a card layout to switch between
 * different functional panels (Home, Products, Reports, Backup, Logs,
 * Developer). Each panel encapsulates a major feature of the system.
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

    private JPanel navPanel;
    private JButton btnHome, btnProducts, btnReports, btnBackup, btnLog, btnDeveloper;

    /**
     * Constructs the main application window with predefined dimensions and
     * initialises the graphical interface components.
     */
    public MainWindow() {
        setTitle("Inventory Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    /**
     * Initialises the overall user interface, including navigation and content
     * panels.
     */
    private void initUI() {
        initNavigationPanel();
        initCardPanels();
        setupLayoutAndListeners();
    }

    /**
     * Creates and styles the navigation panel with buttons for each functional
     * view. Buttons allow users to switch between key sections of the system.
     */
    private void initNavigationPanel() {
        navPanel = new JPanel();
        stylePanel(navPanel);

        btnHome = new JButton("Home");
        btnProducts = new JButton("Products");
        btnReports = new JButton("Reports");
        btnBackup = new JButton("Backup");
        btnLog = new JButton("Logs");
        btnDeveloper = new JButton("Developer");

        styleButton(btnHome);
        styleButton(btnProducts);
        styleButton(btnReports);
        styleButton(btnBackup);
        styleButton(btnLog);
        styleButton(btnDeveloper);

        navPanel.add(btnHome);
        navPanel.add(btnProducts);
        navPanel.add(btnReports);
        navPanel.add(btnBackup);
        navPanel.add(btnLog);
        navPanel.add(btnDeveloper);
    }

    /**
     * Sets up the card panel which holds all the main functional panels.
     * Each panel is registered with a unique identifier for switching views.
     */
    private void initCardPanels() {
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
    }

    /**
     * Applies layout to the main frame and assigns button actions to switch between panels.
     */
    private void setupLayoutAndListeners() {
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
