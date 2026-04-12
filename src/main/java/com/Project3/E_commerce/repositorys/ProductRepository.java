package com.Project3.E_commerce.repositorys;

import com.Project3.E_commerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}