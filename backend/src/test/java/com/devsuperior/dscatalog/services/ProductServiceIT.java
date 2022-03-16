package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourcesNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceIT {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long nonExistingId;
    private long totalDatabaseRecords;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        totalDatabaseRecords = 25L;
    }

    @Test
    public void deleteShouldDeleteProductWhenIdExits () {
        service.delete(existingId);
        Assertions.assertEquals(totalDatabaseRecords - 1, repository.count());
    }

    @Test
    public void deleteShouldThrowResourcesNotFoundExceptionWhenIdDoesNotExits () {
        Assertions.assertThrows(ResourcesNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

}
