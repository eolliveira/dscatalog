package com.devsuperior.dscatalog.services;
import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        List<Category> list = repository.findAll();
        return list.stream().map(c -> new CategoryDTO(c)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id){
       Optional<Category> opt = repository.findById(id);
       Category cat = opt.orElseThrow(() -> new EntityNotFoundException("entity not found"));
        return new CategoryDTO(cat);
    }

    @Transactional
    public CategoryDTO save(Category category){
        Category newCategory = repository.saveAndFlush(category);
        return new CategoryDTO(newCategory);
    }
}
