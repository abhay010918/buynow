package com.buynow.service;

import com.buynow.entity.Product;

import java.util.List;


public interface ProductService {

    Product addProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    Product getProductByName(String productName);
    Product getProductById(Long id);
    List<Product> getAllProducts();

}
