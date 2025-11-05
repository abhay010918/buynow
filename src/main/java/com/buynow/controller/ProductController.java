package com.buynow.controller;

import com.buynow.entity.Product;
import com.buynow.service.ProductService;
import com.buynow.service.S3Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final S3Service s3Service;

    public ProductController(ProductService productService, S3Service s3Service) {
        this.productService = productService;
        this.s3Service = s3Service;
    }


    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("stock") Integer stock,
            @RequestParam("file") MultipartFile file) throws IOException {

        String url = s3Service.uploadFile(file);

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setImageUrl(url);

        return ResponseEntity.ok(productService.addProduct(product));
    }



    // ✅ Only Admin can update products
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }



    // ✅ Anyone (Admin/User) can view products
    @GetMapping("/name")
    public ResponseEntity<Product> getProductByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.getProductByName(name));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // ✅ Download endpoint
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        byte[] fileData = s3Service.downloadFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileData);
    }

    // ✅ Delete endpoint
//    @DeleteMapping("/delete/{fileName}")
//    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
//        s3Service.deleteFile(fileName);
//        return ResponseEntity.ok("File deleted successfully: " + fileName);
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        // 1. Get product by id
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.badRequest().body("❌ Product not found with ID: " + id);
        }

        // 2. Delete image from S3 if exists
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            String imageUrl = product.getImageUrl();
            // Extract the key from URL → everything after ".com/"
            String key = imageUrl.substring(imageUrl.indexOf(".com/") + 5);
            s3Service.deleteFile(key);
        }

        // 3. Delete product from DB
        productService.deleteProduct(id);

        return ResponseEntity.ok("✅ Product and image deleted successfully!");
    }

}
