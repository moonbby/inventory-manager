/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import managers.DeveloperManager;
import static utils.ThemeManager.*;
import static utils.DialogUtils.*;

/**
 * A GUI panel for accessing developer tools to reset application data.
 *
 * Offers buttons to reset product data, log history, and backup records.
 *
 * This panel interacts with DeveloperManager for reset logic.
 */
public class DeveloperPanel extends JPanel {

    private final DeveloperManager devManager;
    private final ProductPanel productPanel;
    private final LogPanel logPanel;
    private final BackupPanel backupPanel;

    /**
     * Constructs the developer panel and initialises its layout and actions.
     *
     * @param devManager handles data reset operations
     * @param productPanel the product panel to be refreshed after reset
     * @param logPanel the log panel to be refreshed after reset
     * @param backupPanel the backup panel to be refreshed after reset
     */
    public DeveloperPanel(DeveloperManager devManager, ProductPanel productPanel,
            LogPanel logPanel, BackupPanel backupPanel) {
        this.devManager = devManager;
        this.productPanel = productPanel;
        this.logPanel = logPanel;
        this.backupPanel = backupPanel;

        setLayout(new BorderLayout());
        stylePanel(this);

        initDevActions();
    }

    /**
     * Initialises developer buttons for resetting Products, Logs, and Backup
     * tables. Adds the buttons to a styled container panel.
     */
    public void initDevActions() {
        JButton btnResetProducts = new JButton("Reset Products");
        JButton btnResetBackup = new JButton("Reset Backup");
        JButton btnResetLogs = new JButton("Reset Logs");
        
        styleButton(btnResetProducts);
        styleButton(btnResetBackup);
        styleButton(btnResetLogs);
        
        btnResetProducts.addActionListener(e -> handleResetProducts());
        btnResetBackup.addActionListener(e -> handleResetBackup());
        btnResetLogs.addActionListener(e -> handleResetLogs());
        
        JPanel btnPanel = new JPanel();
        stylePanel(btnPanel);
        btnPanel.setBorder(createSectionBorder("DEVELOPER TOOLS"));

        btnPanel.add(btnResetProducts);
        btnPanel.add(btnResetBackup);
        btnPanel.add(btnResetLogs);

        add(btnPanel, BorderLayout.CENTER);
    }

    /**
     * Resets the product table and refreshes the UI.
     */
    private void handleResetProducts() {
        devManager.resetProductTable();
        productPanel.refreshProductTable();
        showSuccess(this, "Products reset successfully!");
    }
    
    /**
     * Resets the backup table and refreshes the UI.
     */
    private void handleResetBackup() {
        devManager.resetBackupTable();
        backupPanel.refreshBackupTable();
        showSuccess(this, "Backup reset successfully!");
    }

    /**
     * Resets the log table and refreshes the UI.
     */
    private void handleResetLogs() {
        devManager.resetLogTable();
        logPanel.refreshLogTable();
        showSuccess(this, "Logs reset successfully!");
    }
}
