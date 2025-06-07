/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import interfaces.IInventoryManager;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import managers.LogManager;
import models.Product;

/**
 *
 * @author lifeo
 */
public class ProductPanel extends JPanel {

    private IInventoryManager inventoryManager;
    private LogManager logManager;
    private JTable table;
    private DefaultTableModel tableModel;

    public ProductPanel(IInventoryManager inventoryManager, LogManager logManager) {
        this.inventoryManager = inventoryManager;
        this.logManager = logManager;

        setLayout(new BorderLayout());
        initAddProductForm();
        initProductTable();
        initProductActions();
    }

    public void initAddProductForm() {
        JComboBox<String> cmbType = new JComboBox();
        cmbType.addItem("Clothing");
        cmbType.addItem("Toy");

        JTextField txtName = new JTextField("Name", 20);
        JTextField txtQuantity = new JTextField("Quantity", 5);
        JTextField txtPrice = new JTextField("Price", 4);
        JButton btnAdd = new JButton("Add");

        btnAdd.addActionListener(e
                -> handleAddProduct(cmbType, txtName, txtQuantity, txtPrice));

        JPanel topPanel = new JPanel();
        topPanel.add(cmbType);
        topPanel.add(txtName);
        topPanel.add(txtQuantity);
        topPanel.add(txtPrice);
        topPanel.add(btnAdd);
        add(topPanel, BorderLayout.NORTH);
    }

    public void initProductTable() {
        String[] columns = {"ID", "Type", "Name", "Quantity", "Price"};
        tableModel = new DefaultTableModel(columns, 0);

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JPanel centPanel = new JPanel();
        centPanel.add(new JScrollPane(table));
        add(centPanel, BorderLayout.CENTER);

        refreshTable();
    }

    public void initProductActions() {
        JButton btnRemove = new JButton("Remove");
        JButton btnRestock = new JButton("Restock");
        JButton btnPurchase = new JButton("Purchase");

        btnRemove.addActionListener(e -> handleRemoveProduct());
        btnRestock.addActionListener(e -> handleRestockProduct());
        btnPurchase.addActionListener(e -> handlePurchaseProduct());

        JPanel botPanel = new JPanel();
        botPanel.add(btnRemove);
        botPanel.add(btnRestock);
        botPanel.add(btnPurchase);
        add(botPanel, BorderLayout.SOUTH);
    }

    private void handleAddProduct(JComboBox<String> cmbType, JTextField txtName,
            JTextField txtQuantity, JTextField txtPrice) {
        try {
            String name = txtName.getText();
            String quantityStr = txtQuantity.getText();
            String priceStr = txtPrice.getText();

            if (name.equals("Name") || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid product name.",
                        "Invalid Name", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            double price = Double.parseDouble(priceStr);

            Product added = inventoryManager.addProduct(
                    cmbType.getSelectedItem().toString(), name, quantity, price
            );

            if (added == null) {
                JOptionPane.showMessageDialog(this,
                        "Failed to add product. Please check that all fields are valid.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Product added successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                cmbType.setSelectedIndex(0);
                txtName.setText("Name");
                txtQuantity.setText("Quantity");
                txtPrice.setText("Price");
                refreshTable();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRemoveProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String productId = table.getValueAt(selectedRow, 0).toString();
            inventoryManager.removeProduct(productId);
            JOptionPane.showMessageDialog(this, "Product removed successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        }
    }

    private void handleRestockProduct() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String productId = table.getValueAt(selectedRow, 0).toString();
                String input = JOptionPane.showInputDialog(this, "Enter quantity to restock:");
                int quantity = Integer.parseInt(input);

                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "The quantity must be more than 0.",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } else {
                    inventoryManager.addQuantity(productId, quantity);
                    JOptionPane.showMessageDialog(this, "Product restocked successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handlePurchaseProduct() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String productId = table.getValueAt(selectedRow, 0).toString();
                String input = JOptionPane.showInputDialog(this, "Enter quantity to purchase:");
                int quantity = Integer.parseInt(input);
                int currentQuantity = inventoryManager.getProduct(productId).getQuantity();

                if (quantity > currentQuantity) {
                    JOptionPane.showMessageDialog(this, "The quantity must not exceed " + currentQuantity + ".",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } else {
                    inventoryManager.reduceQuantity(productId, quantity);
                    JOptionPane.showMessageDialog(this, "Product purchased successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshTable() {
        List<Product> products = inventoryManager.getAllProducts();
        tableModel.setRowCount(0);
        for (Product p : products) {
            tableModel.addRow(new Object[]{
                p.getID(),
                p.getProductType(),
                p.getName(),
                p.getQuantity(),
                p.getPrice()
            });
        }
    }
}
