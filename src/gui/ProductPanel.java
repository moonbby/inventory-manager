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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import models.Product;
import static utils.ThemeManager.*;
import static utils.InputValidator.*;
import static utils.DialogUtils.*;
import static utils.TableUtils.populateProductTable;

/**
 *
 * @author lifeo
 */
public class ProductPanel extends JPanel {

    private final IInventoryManager inventoryManager;
    private JTable table;
    private DefaultTableModel tableModel;

    public ProductPanel(IInventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;

        setLayout(new BorderLayout());
        stylePanel(this);

        initProductForm();
        initProductTable();
        initProductActions();
    }

    public void initProductForm() {
        JLabel lblType = new JLabel("Type:");
        styleSubLabel(lblType);
        JComboBox<String> cmbType = new JComboBox<>();
        cmbType.addItem("Clothing");
        cmbType.addItem("Toy");

        JLabel lblName = new JLabel("Name:");
        styleSubLabel(lblName);
        JTextField txtName = new JTextField(20);

        JLabel lblQuantity = new JLabel("Quantity:");
        styleSubLabel(lblQuantity);
        JTextField txtQuantity = new JTextField(5);

        JLabel lblPrice = new JLabel("Price:");
        styleSubLabel(lblPrice);
        JTextField txtPrice = new JTextField(4);

        JButton btnAdd = new JButton("Add");
        styleButton(btnAdd);

        btnAdd.addActionListener(e
                -> handleAddProduct(cmbType, txtName, txtQuantity, txtPrice));

        JPanel topPanel = new JPanel();
        stylePanel(topPanel);
        topPanel.setBorder(createSectionBorder("ADD PRODUCT"));

        topPanel.add(lblType);
        topPanel.add(cmbType);

        topPanel.add(lblName);
        topPanel.add(txtName);

        topPanel.add(lblQuantity);
        topPanel.add(txtQuantity);

        topPanel.add(lblPrice);
        topPanel.add(txtPrice);

        topPanel.add(btnAdd);

        add(topPanel, BorderLayout.NORTH);
    }

    public void initProductTable() {
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

        refreshProductTable();
    }

    public void initProductActions() {
        JButton btnRemove = new JButton("Remove");
        JButton btnRestock = new JButton("Restock");
        JButton btnPurchase = new JButton("Purchase");

        styleButton(btnRemove);
        styleButton(btnRestock);
        styleButton(btnPurchase);

        btnRemove.addActionListener(e -> handleRemoveProduct());
        btnRestock.addActionListener(e -> handleRestockProduct());
        btnPurchase.addActionListener(e -> handlePurchaseProduct());

        JPanel botPanel = new JPanel();
        stylePanel(botPanel);
        botPanel.setBorder(createSectionBorder("ACTIONS"));

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

            if (!isValidProductName(name)) {
                showError(this, "Please enter a valid product name.");
                return;
            }

            if (!isValidProductQuantity(quantityStr)) {
                showError(this, "Quantity must be 0 or greater.");
                return;
            }

            if (!isValidProductPrice(priceStr)) {
                showError(this, "Price must be greater than 0.");
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            double price = Double.parseDouble(priceStr);

            Product added = inventoryManager.addProduct(
                    cmbType.getSelectedItem().toString(), name, quantity, price
            );

            if (added == null) {
                showError(this, "Failed to add product. Please check that all fields are valid.");
            } else {
                refreshProductTable();
                showSuccess(this, "Product added successfully!");
                cmbType.setSelectedIndex(0);
                txtName.setText("");
                txtQuantity.setText("");
                txtPrice.setText("");
            }
        } catch (NumberFormatException ex) {
            showError(this, "Please enter a valid number.");;
        }
    }

    private void handleRemoveProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        String productId = table.getValueAt(selectedRow, 0).toString();
        inventoryManager.removeProduct(productId);
        refreshProductTable();
        showSuccess(this, "Product removed successfully!");
    }

    private void handleRestockProduct() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                return;
            }

            String productId = table.getValueAt(selectedRow, 0).toString();
            String input = promptInput(this, "Enter quantity to restock:");
            if (input == null || input.trim().isEmpty()) {
                return;
            }
            if (!isValidRestockAmount(input)) {
                showError(this, "The quantity must be more than 0.");
                return;
            }

            int quantity = Integer.parseInt(input);
            inventoryManager.addQuantity(productId, quantity);
            refreshProductTable();
            showSuccess(this, "Product restocked successfully!");
        } catch (NumberFormatException ex) {
            showError(this, "Please enter a valid number.");
        }
    }

    private void handlePurchaseProduct() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                return;
            }

            String productId = table.getValueAt(selectedRow, 0).toString();
            String input = promptInput(this, "Enter quantity to purchase:");
            if (input == null || input.trim().isEmpty()) {
                return;
            }

            int currentQuantity = inventoryManager.getProduct(productId).getQuantity();
            if (!isValidPurchaseAmount(input, currentQuantity)) {
                showError(this, "Please enter a valid quantity (1 – " + currentQuantity + ").");
                return;
            }

            int quantity = Integer.parseInt(input);
            inventoryManager.reduceQuantity(productId, quantity);
            refreshProductTable();
            showSuccess(this, "Product purchased successfully!");

        } catch (NumberFormatException ex) {
            showError(this, "Please enter a valid number.");
        }
    }

    public void refreshProductTable() {
        List<Product> products = inventoryManager.getAllProducts();
        if (products == null) {
            return;
        }
        
        populateProductTable(tableModel, products);
    }
}
