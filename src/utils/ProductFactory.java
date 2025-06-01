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
 *
 * @author lifeo
 */
public class ProductFactory {

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
            System.err.println("Unrecognised product type: '" + type + "' during result set mapping.");
            return null;
        }
    }

}
