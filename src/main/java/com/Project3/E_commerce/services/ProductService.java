package com.Project3.E_commerce.services;

import com.Project3.E_commerce.exceptions.InformationNotFoundException;
import com.Project3.E_commerce.models.Category;
import com.Project3.E_commerce.models.Product;
import com.Project3.E_commerce.repositorys.CategoryRepository;
import com.Project3.E_commerce.repositorys.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final ConcurrentHashMap<Long, ReentrantLock> productLocks = new ConcurrentHashMap<>();

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new InformationNotFoundException("Product not found"));
    }

    public Product createProduct(Product product ,Long categoryId)
    {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new InformationNotFoundException("Category with ID " + categoryId + " not found"));
        product.setCategory(category);
        return productRepository.save(product);
    }



    public Product updateProduct(Long id, Product updated) {
        Product existing = getProductById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setStockQuantity(updated.getStockQuantity());
        existing.setCategory(updated.getCategory());
        return productRepository.save(existing);
    }


    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) throw new InformationNotFoundException("Product not found");
        productRepository.deleteById(id);
    }


    @Transactional
    public boolean controlStock(Long productId, int quantity) {

        ReentrantLock lock = productLocks.computeIfAbsent(productId, k -> new ReentrantLock());

        lock.lock();
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new InformationNotFoundException("Product not found: " + productId));

            if (product.getStockQuantity() >= quantity) {
                product.setStockQuantity(product.getStockQuantity() - quantity);
                productRepository.save(product);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
}
