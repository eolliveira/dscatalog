package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.Instant;
import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    private long nonExistentId;
    private long existingId;

    @BeforeEach
    void setUp() {
        nonExistentId = 121212L;
        existingId = 2L;
    }

    @Test
    public void findByIdShoulReturnOptionalNotEmptyWhenIdExists(){
        Optional<Product> p = repository.findById(existingId);
        p.isEmpty();

        Assertions.assertFalse(p.isEmpty());
    }

    @Test
    public void findByIdShoulReturnOptionalEmptyWhenIdDoesNotExist(){
        Optional<Product> p = repository.findById(nonExistentId);
        p.isEmpty();

        Assertions.assertTrue(p.isEmpty());
    }



    @Test
    public void saveShouldAutoIncrementIdWhenIdIsNull(){
        Product p = Factory.createProduct();
        p.setId(null);

        p = repository.save(p);

        Assertions.assertNotNull(p.getId());
        Assertions.assertEquals(26, p.getId());

    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        repository.deleteById(existingId);
        Optional<Product> product = repository.findById(existingId);

        Assertions.assertFalse(product.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(nonExistentId);
        });
    }
}
