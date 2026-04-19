package com.Project3.E_commerce.repositorys;

import com.Project3.E_commerce.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String categoryName);
    Optional<Category> findByName(String name);
}
