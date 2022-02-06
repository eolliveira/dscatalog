package com.devsuperior.dscatalog.services;
import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourcesNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable){
        Page<Category> list = repository.findAll(pageable);
        return list.map(CategoryDTO::new);
        //return list.stream().map(c -> new CategoryDTO(c)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id){
       Optional<Category> opt = repository.findById(id);
       Category cat = opt.orElseThrow(() -> new ResourcesNotFoundException("entity not found"));
        return new CategoryDTO(cat);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto){
        Category category = new Category();
        category.setName(dto.getName());
        category = repository.save(category);
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
            Category category = repository.getById(id);
            category.setName(dto.getName());
            category = repository.save(category);
            return new CategoryDTO(category);
        }
        catch (EntityNotFoundException e){
          throw new ResourcesNotFoundException("resource Id:" + id + " not found");
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            Category category = repository.getById(id);
            repository.delete(category);
        }
        catch (EntityNotFoundException e){
            throw new ResourcesNotFoundException("resource Id:" + id + " not found");
        }
        catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Database integrity violation");
        }
    }
}
