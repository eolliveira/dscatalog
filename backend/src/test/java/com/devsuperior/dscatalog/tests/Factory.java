package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        return new Product(1L, "Computador teste", "description", 100.0, "httpimage", Instant.now());
    }
}
