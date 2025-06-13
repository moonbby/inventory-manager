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
 * A GUI panel for displaying backed-up inventory records.
 *
 * Provides the ability to trigger a manual backup of the current product list
 * and view existing backup data in a read-only table.
 *
 * This panel interacts with BackupManager for business logic.
 */
public class BackupPanel extends JPanel {

    private final BackupManager backupManager;
    private JTable table;
    private DefaultTableModel tableModel;

    /**
     * Constructs the backup panel and initialises its layout and actions.
     *
     * @param backupManager the manager responsible for handling backup logic
     */
    public BackupPanel(BackupManager backupManager) {
        this.backupManager = backupManager;

        setLayout(new BorderLayout());
        stylePanel(this);

        initBackupActions();
        initBackupTable();
    }

    /**
     * Initialises the backup trigger button and places it in a styled panel.
     * Adds an action listener to allow users to manually back up inventory.
     */
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

    /**
     * Configures the table for displaying backed-up products. Sets up column
     * names, disables editing, and wraps in a scroll pane.
     */
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

    /**
     * Handles the backup action by calling the manager, refreshing the UI, and
     * displaying a success message.
     */
    private void handleBackup() {
        backupManager.backupInventory();
        refreshBackupTable();
        showSuccess(this, "Inventory backed up successfully!");
    }

    /**
     * Refreshes the backup table with the latest data from the manager. Clears
     * and repopulates the table using a utility method.
     */
    public void refreshBackupTable() {
        List<Product> products = backupManager.getBackup();
        populateProductTable(tableModel, products);
    }
}
