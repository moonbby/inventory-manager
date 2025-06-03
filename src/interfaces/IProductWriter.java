/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import models.Product;

/**
 *
 * @author lifeo
 */
public interface IProductWriter {
    
    Product addProduct(String type, String name, int quantity, double price);

    boolean removeProduct(String id);

    boolean updateQuantity(String id, int newQuantity);
}
