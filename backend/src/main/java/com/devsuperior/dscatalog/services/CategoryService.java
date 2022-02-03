package com.devsuperior.dscatalog.services;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public List<Category> findAll(){
        return repository.findAll();
    }

    public Category findById(Long id){
        Category newCategory = repository.findById(id).get();
        return newCategory;
    }

    @Transactional
    public Category save(Category category){
        Category newCategory = repository.saveAndFlush(category);
        return newCategory;
    }

}
