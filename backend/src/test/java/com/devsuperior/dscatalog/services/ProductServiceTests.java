package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourcesNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    private long nonExistentId;
    private long existingId;
    private long dependentId;

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        nonExistentId = 121212L;
        existingId = 2L;
        dependentId = 4L;

        doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistentId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(() -> service.delete(existingId));

        //verifica se foi feita alguma chamada expecifica ao Mock(repository)
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourcesNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourcesNotFoundException.class, () -> service.delete(nonExistentId));

        Mockito.verify(repository).deleteById(nonExistentId);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId(){
        Assertions.assertThrows(DataBaseException.class, () -> service.delete(nonExistentId));

        Mockito.verify(repository).deleteById(nonExistentId);
    }








}
