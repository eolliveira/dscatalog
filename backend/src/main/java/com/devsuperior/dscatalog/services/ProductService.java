package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourcesNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable){
        Page<Product> list = repository.findAll(pageable);
        return list.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Optional<Product> opt = repository.findById(id);
        Product p = opt.orElseThrow(() -> new ResourcesNotFoundException("Product id: " + id + " not found"));
        return new ProductDTO(p, p.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto){
        Product p = new Product();
        copyDtoToEntity(dto,p);
        p = repository.save(p);
        return new ProductDTO(p, p.getCategories());
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product product = repository.getOne(id);
            copyDtoToEntity(dto, product);
            product = repository.save(product);
            return new ProductDTO(product, product.getCategories());
        }
        catch (EntityNotFoundException e){
            throw new ResourcesNotFoundException("Product id: " + id + " not found");
        }

    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourcesNotFoundException("Product id: " + id + " not found");
        }
        catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Database integrity violation");
        }

    }

    private void copyDtoToEntity(ProductDTO dto, Product product) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImgUrl(dto.getImgUrl());
        product.setDate(dto.getDate());

        product.getCategories().clear();
        for (CategoryDTO c : dto.getCategories()) {
            try {
                Category cat = categoryRepository.getOne(c.getId());
                product.getCategories().add(cat);
            }
            catch (EntityNotFoundException e) {
                throw new ResourcesNotFoundException("Category id: " + c.getId() + " not found");
            }
        }
    }
}
