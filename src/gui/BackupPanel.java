/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import managers.BackupManager;
import models.Product;
import static utils.ThemeManager.*;
import static utils.DialogUtils.*;
import static utils.TableUtils.populateProductTable;

/**
 *
 * @author lifeo
 */
public class BackupPanel extends JPanel {

    private final BackupManager backupManager;
    private JTable table;
    private DefaultTableModel tableModel;

    public BackupPanel(BackupManager backupManager) {
        this.backupManager = backupManager;

        setLayout(new BorderLayout());
        stylePanel(this);
        
        initBackupActions();
        initBackupTable();
    }

    public void initBackupActions() {
        JButton btnBackup = new JButton("Backup");
        styleButton(btnBackup);

        btnBackup.addActionListener(e -> handleBackup());

        JPanel topPanel = new JPanel();
        stylePanel(topPanel);
        topPanel.setBorder(createSectionBorder("BACKUP ACTIONS"));
        
        topPanel.add(btnBackup);
        add(topPanel, BorderLayout.NORTH);
    }

    public void initBackupTable() {
        String[] columns = {"ID", "Type", "Name", "Quantity", "Price"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JPanel centPanel = new JPanel();
        stylePanel(centPanel);
        centPanel.add(new JScrollPane(table));
        add(centPanel, BorderLayout.CENTER);

        refreshBackupTable();
    }

    private void handleBackup() {
        backupManager.backupInventory();
        refreshBackupTable();
        showSuccess(this, "Inventory backed up successfully!");
    }

    public void refreshBackupTable() {
        List<Product> products = backupManager.getBackup();
        populateProductTable(tableModel, products);
    }
}
