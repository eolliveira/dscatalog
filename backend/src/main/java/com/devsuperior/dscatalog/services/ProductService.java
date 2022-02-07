package com.devsuperior.dscatalog.services;
import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourcesNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable){
        Page<Product> list = repository.findAll(pageable);
        return list.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Optional<Product> opt = repository.findById(id);
        Product p = opt.orElseThrow(() -> new ResourcesNotFoundException("entity not found"));
        return new ProductDTO(p, p.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto){
        Product p = new Product();

        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setImgUrl(dto.getImgUrl());
        p.setDate(dto.getDate());

        for (CategoryDTO c : dto.getCategories()) {
            p.getCategories().add(new Category(c));
        }

        p = repository.save(p);

        return new ProductDTO(p, p.getCategories());
    }
}
