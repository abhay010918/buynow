package com.buynow.service.impl;

import com.buynow.entity.Product;
import com.buynow.exeptions.ResourceNotFoundException;
import com.buynow.repository.ProductRepository;
import com.buynow.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }


    @Override
    public Product updateProduct(Long id, Product product) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        existing.setImageUrl(product.getImageUrl());

        return productRepository.save(existing);

    }

    @Override
    public void deleteProduct(Long id) {

        if(!productRepository.existsById(id)){
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    @Override
    public Product getProductByName(String productName) {
        return productRepository.findByName(productName)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("product not found by this id" + id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
