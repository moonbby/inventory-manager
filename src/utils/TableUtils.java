/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import models.Product;

/**
 * Utility class for populating JTable models with product data. Supports
 * consistent table population for GUI components.
 */
public class TableUtils {

    /**
     * Populates the given table model with product entries. Clears existing
     * rows before inserting new entries to ensure the table reflects current
     * data.
     *
     * @param model the table model to populate
     * @param products the list of products to display
     */
    public static void populateProductTable(DefaultTableModel model, List<Product> products) {
        model.setRowCount(0);
        for (Product p : products) {
            model.addRow(new Object[]{
                p.getID(),
                p.getProductType(),
                p.getName(),
                p.getQuantity(),
                p.getPrice()
            });
        }
    }
}
