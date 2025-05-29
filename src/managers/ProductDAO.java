/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import models.ClothingProduct;
import models.Product;
import models.ToyProduct;

/**
 *
 * @author lifeo
 */
public class ProductDAO {

    private final DatabaseManager dbManager;
    private final Connection conn;

    public ProductDAO() {
        dbManager = new DatabaseManager();
        conn = dbManager.getConnection();
    }

    public void addProduct(Product product) {
        try {
            String sql = "INSERT INTO PRODUCTS VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, product.getID());
            ps.setString(2, product.getName());
            ps.setInt(3, product.getQuantity());
            ps.setDouble(4, product.getPrice());
            ps.setString(5, product.getProductType());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("SQLException during adding product: " + ex.getMessage());
        }
    }

    public void removeProduct(String id) {
        try {
            String sql = "DELETE FROM PRODUCTS WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("SQLException during removing product: " + ex.getMessage());
        }
    }

    public void updateQuantity(String id, int newQuantity) {
        try {
            String sql = "UPDATE PRODUCTS SET QUANTITY = ? WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, newQuantity);
            ps.setString(2, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("SQLException during updating quantity: " + ex.getMessage());
        }
    }

    public Product getProduct(String id) {
        Product product = null;
        try {
            String sql = "SELECT * FROM PRODUCTS WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString(2);
                int quantity = rs.getInt(3);
                double price = rs.getDouble(4);
                String type = rs.getString(5);

                if (type.equalsIgnoreCase("Clothing")) {
                    product = new ClothingProduct(id, name, quantity, price);
                } else if (type.equalsIgnoreCase("Toy")) {
                    product = new ToyProduct(id, name, quantity, price);
                }
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            System.err.println("SQLException during getting product: " + ex.getMessage());
        }
        return product;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try {
            String sql = "SELECT * FROM PRODUCTS";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString(2);
                int quantity = rs.getInt(3);
                double price = rs.getDouble(4);
                String type = rs.getString(5);

                if (type.equalsIgnoreCase("Clothing")) {
                    products.add(new ClothingProduct(id, name, quantity, price));
                } else if (type.equalsIgnoreCase("Toy")) {
                    products.add(new ToyProduct(id, name, quantity, price));
                }
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            System.err.println("SQLException during getting all products: " + ex.getMessage());
        }
        return products;
    }

    public List<Product> getLowStockProducts(int threshold) {
        List<Product> products = new ArrayList<>();
        try {
            String sql = "SELECT * FROM PRODUCTS\n"
                    + "WHERE QUANTITY < ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, threshold);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString(2);
                int quantity = rs.getInt(3);
                double price = rs.getDouble(4);
                String type = rs.getString(5);

                if (type.equalsIgnoreCase("Clothing")) {
                    products.add(new ClothingProduct(id, name, quantity, price));
                } else if (type.equalsIgnoreCase("Toy")) {
                    products.add(new ToyProduct(id, name, quantity, price));
                }
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            System.err.println("SQLException during getting low stock products: " + ex.getMessage());
        }
        return products;
    }
}
