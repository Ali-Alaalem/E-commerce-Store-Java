package com.Project3.E_commerce.controllers;

import com.Project3.E_commerce.models.Category;
import com.Project3.E_commerce.models.User;
import com.Project3.E_commerce.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAll()
    {
        return categoryService.getAll();
    }

    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Category getById(@PathVariable Long categoryId)
    {
        return categoryService.getById(categoryId);
    }

    @PostMapping("/createCategory")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Category create(@RequestBody Category category)
    {
        return categoryService.createCategory(category);
    }

    @PutMapping("/updateCategory/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Category update(@PathVariable Long categoryId, @RequestBody Category category) {
        return categoryService.updateCategory(categoryId, category);
    }

    @DeleteMapping("/deleteCategory/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }

}