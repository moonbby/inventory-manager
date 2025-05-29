/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author lifeo
 */
public class ProductTableSeeder {

    private final DatabaseManager dbManager;
    private final Connection conn;
    private Statement statement;

    public ProductTableSeeder() {
        dbManager = new DatabaseManager();
        conn = dbManager.getConnection();
    }

    public void createProductsTableWithSeedData() {
        try {
            if (!tableExists("PRODUCTS")) {
                this.statement = conn.createStatement();

                this.statement.addBatch("CREATE TABLE PRODUCTS (ID VARCHAR(10) PRIMARY KEY, "
                        + "NAME VARCHAR(50), QUANTITY INT, PRICE DOUBLE, TYPE VARCHAR(20))");

                this.statement.addBatch("INSERT INTO PRODUCTS VALUES ('P001', 'Jeans', 51, 120.5, 'Clothing'),\n"
                        + "('P002', 'Pajamas', 7, 78.89, 'Clothing'),\n"
                        + "('P003', 'Chess', 72, 34.66, 'Toy'),\n"
                        + "('P004', 'Console', 11, 89.99, 'Toy'),\n"
                        + "('P005', 'Jacket', 5, 89.0, 'Clothing'),\n"
                        + "('P006', 'Dress', 23, 56.99, 'Clothing'),\n"
                        + "('P007', 'T-Shirt', 36, 50.99, 'Clothing'),\n"
                        + "('P008', 'Teddy Bear', 100, 21.99, 'Toy'),\n"
                        + "('P009', 'Slime', 99, 13.5, 'Toy')");

                this.statement.executeBatch();
                System.out.println("PRODUCTS table created and seeded successfully.");
            } else {
                System.out.println("PRODUCTS table already exists. No action taken.");
            }
        } catch (SQLException ex) {
            System.err.println("SQLException during seeding: " + ex.getMessage());
        }
    }

    private boolean tableExists(String tableName) {
        try {
            DatabaseMetaData dbMeta = this.conn.getMetaData();
            ResultSet rs = dbMeta.getTables(null, null, tableName.toUpperCase(), new String[]{"TABLE"});
            boolean exists = rs.next();
            rs.close();
            return exists;
        } catch (SQLException ex) {
            System.err.println("SQLException checking table: " + ex.getMessage());
            return false;
        }
    }

    public void closeConnection() {
        this.dbManager.closeConnections();
    }
}
