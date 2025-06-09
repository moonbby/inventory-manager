/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import managers.LogManager;

/**
 *
 * @author lifeo
 */
public class LogPanel extends JPanel {

    private LogManager logManager;
    private JTable table;
    private DefaultTableModel tableModel;

    public LogPanel(LogManager logManager) {
        this.logManager = logManager;

        setLayout(new BorderLayout());
        initLogActions();
        initLogTable();
    }

    public void initLogActions() {
        JButton btnRefresh = new JButton("Refresh");

        btnRefresh.addActionListener(e -> handleRefreshLogs());

        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createTitledBorder("LOGS"));
        topPanel.add(btnRefresh);
        add(topPanel, BorderLayout.NORTH);
    }

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
        centerWrapper.add(scrollPane);
        add(centerWrapper, BorderLayout.CENTER);

        refreshLogTable();
    }

    private void handleRefreshLogs() {
        refreshLogTable();
    }

    public void refreshLogTable() {
        List<Object[]> logs = logManager.getLogs();
        tableModel.setRowCount(0);
        for (Object[] row : logs) {
            tableModel.addRow(row);
        }
    }
}
