package com.Project3.E_commerce.controllers;

import com.Project3.E_commerce.models.Product;
import com.Project3.E_commerce.services.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/product/{productId}")
    public Product getById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @PostMapping("/createProduct/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product create(@RequestBody Product product ,@PathVariable Long categoryId) {
        return productService.createProduct(product,categoryId);
    }

    @PutMapping("/updateProduct/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product update(@PathVariable Long productId, @RequestBody Product product) {
        return productService.updateProduct(productId, product);
    }

    @DeleteMapping("/deleteProduct/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(@PathVariable Long productId) {
        productService.deleteProduct(productId);
    }
}