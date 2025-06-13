/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import managers.ReportManager;
import models.Product;
import static utils.ThemeManager.*;
import static utils.InputValidator.*;
import static utils.DialogUtils.*;
import static utils.TableUtils.populateProductTable;

/**
 * A GUI panel for generating inventory reports.
 *
 * Supports summary reports, most expensive product lookup, and identifying low
 * stock items. Output is shown in a central table.
 */
public class ReportPanel extends JPanel {

    private ReportManager reportManager;
    private JTable table;
    JScrollPane scrollPane;

    /**
     * Constructs the report panel and initialises its layout and actions.
     *
     * @param reportManager the report manager used to generate report data
     */
    public ReportPanel(ReportManager reportManager) {
        this.reportManager = reportManager;

        setLayout(new BorderLayout());
        stylePanel(this);

        initReportActions();
        initProductTable();
    }

    /**
     * Initialises the report action buttons. Adds buttons for generating
     * summary, most expensive, and low stock reports.
     */
    public void initReportActions() {
        JButton btnSummary = new JButton("Summary Report");
        JButton btnMostExpensive = new JButton("Most Expensive Product");
        JButton btnLowStock = new JButton("Low Stock Product");

        styleButton(btnSummary);
        styleButton(btnMostExpensive);
        styleButton(btnLowStock);

        btnSummary.addActionListener(e -> handleSummary());
        btnMostExpensive.addActionListener(e -> handleMostExpensive());
        btnLowStock.addActionListener(e -> handleLowStock());

        JPanel topPanel = new JPanel();
        stylePanel(topPanel);
        topPanel.setBorder(createSectionBorder("REPORTS"));

        topPanel.add(btnSummary);
        topPanel.add(btnMostExpensive);
        topPanel.add(btnLowStock);

        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Sets up the table used for displaying report data. The table is hidden by
     * default until a report is shown.
     */
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
        stylePanel(centPanel);
        centPanel.add(scrollPane);

        add(centPanel, BorderLayout.CENTER);
    }

    /**
     * Handles generation of a summary report showing product type counts.
     * Displays results in the table.
     */
    private void handleSummary() {
        String[] columns = {"Type", "Count"};
        DefaultTableModel summaryModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<String[]> summary = reportManager.getSummaryCounts();
        if (summary == null) {
            return;
        }

        for (String[] row : summary) {
            summaryModel.addRow(row);
        }

        table.setModel(summaryModel);
        table.setVisible(true);
        scrollPane.setVisible(true);
        revalidate();
        repaint();
    }

    /**
     * Handles fetching the most expensive product and displaying it in the
     * table.
     */
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
            showInfo(this, "No products found.");
        }
    }

    /**
     * Prompts the user for a stock threshold and displays all products with
     * stock below the entered value.
     */
    private void handleLowStock() {
        try {
            String input = promptInput(this, "Enter stock threshold:");
            if (input == null || input.trim().isEmpty()) {
                return;
            }

            if (!isValidLowStockThreshold(input)) {
                showError(this, "The threshold must be over 0.");
                return;
            }

            int threshold = Integer.parseInt(input);
            List<Product> products = reportManager.exportLowStockMenu(threshold);
            refreshReportTable(products);

            table.setVisible(true);
            scrollPane.setVisible(true);
            revalidate();
            repaint();
        } catch (NumberFormatException ex) {
            showError(this, "Please enter a valid number.");
        }
    }

    /**
     * Refreshes the report table using the provided list of products.
     *
     * @param products the products to be displayed in the table
     */
    public void refreshReportTable(List<Product> products) {
        String[] columns = {"ID", "Type", "Name", "Quantity", "Price"};
        DefaultTableModel productModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        populateProductTable(productModel, products);
        table.setModel(productModel);
    }
}
