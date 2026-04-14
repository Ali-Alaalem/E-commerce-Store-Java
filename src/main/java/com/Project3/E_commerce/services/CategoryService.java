package com.Project3.E_commerce.services;

import com.Project3.E_commerce.exceptions.InformationExistException;
import com.Project3.E_commerce.exceptions.InformationNotFoundException;
import com.Project3.E_commerce.models.Category;
import com.Project3.E_commerce.repositorys.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAll() { return categoryRepository.findAll(); }


    public Category getById(Long id)
    {
        return categoryRepository.findById(id).orElseThrow(() -> new InformationNotFoundException("Category not found"));
    }


    public Category createCategory(Category c)
    {
        boolean category = categoryRepository.existsByName(c.getName());
        if(category){
            throw new InformationExistException("Category with name" +c.getName() + "already exist");
        }else{
            return categoryRepository.save(c);
        }
    }


    public Category updateCategory(Long id, Category category) {
        Category existing = getById(id);
        existing.setName(category.getName());
        return categoryRepository.save(existing);
    }


    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id))
        {
            throw new InformationNotFoundException("Category not found");
        }else {
            categoryRepository.deleteById(id);
        }
    }
}