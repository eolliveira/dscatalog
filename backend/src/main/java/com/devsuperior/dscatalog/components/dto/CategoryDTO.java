package com.devsuperior.dscatalog.components.dto;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryDTO implements Serializable {

    private Long id;
    private String name;

    private final List<ProductDTO> products = new ArrayList<>();

    public CategoryDTO(){}

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

    public CategoryDTO(Category category, Set<Product> products) {
        this.id = category.getId();
        this.name = category.getName();
        for (Product p : products) {
            this.products.add(new ProductDTO(p));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }
}
