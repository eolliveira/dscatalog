package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        Long id = 1L;

        repository.deleteById(id);
        Optional<Product> product = repository.findById(id);

        Assertions.assertFalse(product.isPresent());
    }
}
