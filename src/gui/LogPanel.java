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
import managers.LogManager;
import static utils.ThemeManager.*;

/**
 * A GUI panel for displaying application logs.
 *
 * Provides a table view of logged actions and includes a refresh button to
 * retrieve the latest entries from the log manager.
 */
public class LogPanel extends JPanel {

    private LogManager logManager;
    private JTable table;
    private DefaultTableModel tableModel;

    /**
     * Constructs the log panel and initialises its layout and actions.
     *
     * @param logManager the log manager used to retrieve and update logs
     */
    public LogPanel(LogManager logManager) {
        this.logManager = logManager;

        setLayout(new BorderLayout());
        stylePanel(this);

        initLogActions();
        initLogTable();
    }

    /**
     * Initialises the log action controls. Adds a 'Refresh' button to reload
     * the latest logs from the log manager.
     */
    public void initLogActions() {
        JButton btnRefresh = new JButton("Refresh");
        styleButton(btnRefresh);

        btnRefresh.addActionListener(e -> handleRefreshLogs());

        JPanel topPanel = new JPanel();
        stylePanel(topPanel);
        topPanel.setBorder(createSectionBorder("LOGS"));
        topPanel.add(btnRefresh);
        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Initialises the log table layout and configuration. Sets up the table
     * columns, disables cell editing, and applies preferred sizing.
     */
    public void initLogTable() {
        String[] columns = {"ID", "Action", "Timestamp"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.getColumnModel().getColumn(0).setPreferredWidth(35);
        table.getColumnModel().getColumn(1).setPreferredWidth(302);
        table.getColumnModel().getColumn(2).setPreferredWidth(111);

        JScrollPane scrollPane = new JScrollPane(table);
        JPanel centerWrapper = new JPanel();
        stylePanel(centerWrapper);
        centerWrapper.add(scrollPane);
        add(centerWrapper, BorderLayout.CENTER);

        refreshLogTable();
    }

    /**
     * Handles refresh button action by updating the log table.
     */
    private void handleRefreshLogs() {
        refreshLogTable();
    }

    /**
     * Refreshes the table with the latest log entries from the log manager.
     * Clears existing rows and populates the table with up-to-date data.
     */
    public void refreshLogTable() {
        List<Object[]> logs = logManager.getLogs();
        tableModel.setRowCount(0);
        for (Object[] row : logs) {
            tableModel.addRow(row);
        }
    }
}
