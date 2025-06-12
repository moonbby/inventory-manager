/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import managers.DeveloperManager;
import static utils.ThemeManager.*;

/**
 *
 * @author lifeo
 */
public class DeveloperPanel extends JPanel {

    private final DeveloperManager devManager;
    private final ProductPanel productPanel;
    private final LogPanel logPanel;
    private final BackupPanel backupPanel;

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

    public void initDevActions() {
        JButton btnResetProducts = new JButton("Reset Products");
        JButton btnResetLogs = new JButton("Reset Logs");
        JButton btnResetBackup = new JButton("Reset Backup");

        styleButton(btnResetProducts);
        styleButton(btnResetLogs);
        styleButton(btnResetBackup);

        btnResetProducts.addActionListener(e -> handleResetProducts());
        btnResetLogs.addActionListener(e -> handleResetLogs());
        btnResetBackup.addActionListener(e -> handleResetBackup());

        JPanel btnPanel = new JPanel();
        stylePanel(btnPanel);
        btnPanel.setBorder(createSectionBorder("DEVELOPER TOOLS"));
        
        btnPanel.add(btnResetProducts);
        btnPanel.add(btnResetLogs);
        btnPanel.add(btnResetBackup);

        add(btnPanel, BorderLayout.CENTER);
    }

    private void handleResetProducts() {
        devManager.resetProductTable();
        productPanel.refreshProductTable();
        JOptionPane.showMessageDialog(this, "Products reset successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleResetLogs() {
        devManager.resetLogTable();
        logPanel.refreshLogTable();
        JOptionPane.showMessageDialog(this, "Logs reset successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleResetBackup() {
        devManager.resetBackupTable();
        backupPanel.refreshBackupTable();
        JOptionPane.showMessageDialog(this, "Backup reset successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
