package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        return new Product(1L, "Computador teste", "description", 100.0, "httpimage", Instant.now());
    }

    public static Category createCategory(){
        return new Category(1L, "Games");
    }
}
