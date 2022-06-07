package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourcesNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
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

    @Test
    public void findAllShouldReturnPage () {

        PageRequest pageable = PageRequest.of(0, 10);

        /////REVER
        Page<ProductDTO> response = service.findAll(0L,null, pageable);

        Assertions.assertFalse(response.isEmpty());
        Assertions.assertEquals(0, response.getNumber());
        Assertions.assertEquals(10, response.getSize());
        Assertions.assertEquals(totalDatabaseRecords, response.getTotalElements());

    }

    @Test
    public void findAllShouldReturnPageEmptyWhenPagepageDoesNotExist () {

        PageRequest pageable = PageRequest.of(50, 10);


        ////REVER
        Page<ProductDTO> response = service.findAll(0L , null, pageable);

        Assertions.assertTrue(response.isEmpty());

    }

    @Test
    public void findAllShouldReturnPageSortedByName () {

        PageRequest pageable = PageRequest.of(0, 10, Sort.by("name"));




        //////REVER
        Page<ProductDTO> response = service.findAll(0L, null, pageable);

        Assertions.assertFalse(response.isEmpty());
        Assertions.assertEquals("Macbook Pro", response.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", response.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", response.getContent().get(2).getName());

    }



}
