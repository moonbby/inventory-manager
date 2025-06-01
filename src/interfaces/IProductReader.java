/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import models.Product;

/**
 *
 * @author lifeo
 */
public interface IProductReader {
    
    Product getProduct(String id);

    List<Product> getAllProducts();

    List<Product> getLowStockProducts(int threshold);
}
