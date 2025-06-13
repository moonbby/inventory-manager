/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import models.ClothingProduct;
import models.Product;
import models.ToyProduct;
import static utils.DBConstants.*;
import static utils.ProductTypes.*;

/**
 * Factory class responsible for creating Product objects from database records.
 */
public class ProductFactory {

    /**
     * Creates a Product object from the current row in the given ResultSet.
     *
     * Maps the "TYPE" column to the appropriate subclass: ClothingProduct or
     * ToyProduct. If the type value is unrecognised, an
     * IllegalArgumentException is thrown.
     *
     * @param rs the ResultSet pointing to the current product record
     * @return a Product instance representing the database row
     * @throws SQLException if a database access error occurs
     * @throws IllegalArgumentException if the product type in the database is
     * invalid or unsupported
     */
    public static Product createFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString(COL_PRODUCT_ID);
        String name = rs.getString(COL_NAME);
        int quantity = rs.getInt(COL_QUANTITY);
        double price = rs.getDouble(COL_PRICE);
        String type = rs.getString(COL_TYPE);

        if (type.equalsIgnoreCase(CLOTHING)) {
            return new ClothingProduct(id, name, quantity, price);
        } else if (type.equalsIgnoreCase(TOY)) {
            return new ToyProduct(id, name, quantity, price);
        } else {
            throw new IllegalArgumentException("Unrecognised product type in DB: '" + type + "' during result set mapping.");
        }
    }
}
