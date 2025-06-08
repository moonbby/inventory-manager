/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import managers.LogManager;
import managers.ReportManager;
import models.Product;

/**
 *
 * @author lifeo
 */
public class ReportPanel extends JPanel {

    private ReportManager reportManager;
    private LogManager logManager;
    private JTable table;
    JScrollPane scrollPane;

    public ReportPanel(ReportManager reportManager, LogManager logManager) {
        this.reportManager = reportManager;
        this.logManager = logManager;

        setLayout(new BorderLayout());
        initReportActions();
        initProductTable();
    }

    public void initReportActions() {
        JButton btnSummary = new JButton("Summary Report");
        JButton btnMostExpensive = new JButton("Most Expensive Product");
        JButton btnLowStock = new JButton("Low Stock Product");

        btnSummary.addActionListener(e -> handleSummary());
        btnMostExpensive.addActionListener(e -> handleMostExpensive());
        btnLowStock.addActionListener(e -> handleLowStock());

        JPanel topPanel = new JPanel();
        topPanel.add(btnSummary);
        topPanel.add(btnMostExpensive);
        topPanel.add(btnLowStock);

        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("REPORTS"),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        add(topPanel, BorderLayout.NORTH);
    }

    public void initProductTable() {
        table = new JTable();
        
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setFocusable(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setVisible(false);

        scrollPane = new JScrollPane(table);
        scrollPane.setVisible(false);

        JPanel centPanel = new JPanel();
        centPanel.add(scrollPane);
        add(centPanel, BorderLayout.CENTER);
    }

    private void handleSummary() {
        String[] columns = {"Type", "Count"};
        DefaultTableModel summaryModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        List<String[]> summary = reportManager.getSummaryCounts();

        for (String[] row : summary) {
            summaryModel.addRow(row);
        }

        table.setModel(summaryModel);
        table.setVisible(true);
        scrollPane.setVisible(true);
        revalidate();
        repaint();
    }

    private void handleMostExpensive() {
        List<Product> products = new ArrayList<Product>();
        Product expensive = reportManager.getMostExpensiveProduct();
        if (expensive != null) {
            products.add(expensive);
            refreshReportTable(products);

            table.setVisible(true);
            scrollPane.setVisible(true);
            revalidate();
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "No products found.", "Empty", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleLowStock() {
        try {
            String input = JOptionPane.showInputDialog(this, "Enter stock threshold:");
            int threshold = Integer.parseInt(input);

            if (threshold <= 0) {
                JOptionPane.showMessageDialog(this, "The threshold must be over 0.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } else {
                List<Product> products = reportManager.exportLowStockMenu(threshold);
                refreshReportTable(products);

                table.setVisible(true);
                scrollPane.setVisible(true);
                revalidate();
                repaint();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshReportTable(List<Product> products) {
        String[] columns = {"ID", "Type", "Name", "Quantity", "Price"};
        DefaultTableModel productModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        for (Product p : products) {
            productModel.addRow(new Object[]{
                p.getID(),
                p.getProductType(),
                p.getName(),
                p.getQuantity(),
                p.getPrice()
            });
        }
        
        table.setModel(productModel); 
    }
}
