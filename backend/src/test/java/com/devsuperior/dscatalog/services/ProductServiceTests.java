package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourcesNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    private long nonExistentId;
    private long existingId;
    private long dependentId;
    private Product product;
    private Category category;

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        nonExistentId = 121212L;
        existingId = 2L;
        dependentId = 4L;
        product = Factory.createProduct();
        category = Factory.createCategory();
        PageImpl<Product> page = new PageImpl<>(List.of(product));

        Mockito.when(productRepository.getOne(existingId)).thenReturn(product);
        Mockito.when(productRepository.getOne(nonExistentId)).thenThrow(ResourcesNotFoundException.class);

        Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getOne(nonExistentId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(productRepository.findAll((Pageable) any())).thenReturn(page);

        Mockito.when(productRepository.save(any())).thenReturn(product);

        Mockito.when(productRepository.find(any(), any(), any())).thenReturn(page);

        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());


        Mockito.doNothing().when(productRepository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistentId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
    }


    @Test
    public void findAllShouldReturnPage(){
        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductDTO> response = service.findAll(0L, "", pageable);

        Assertions.assertNotNull(response);
    }



    @Test
    public void findByIdShouldReturnProductWhenIdExixts(){
        ProductDTO response = service.findById(existingId);
        Assertions.assertNotNull(response);
        Mockito.verify(productRepository).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourcesNotFoundException.class, () -> service.findById(nonExistentId));
        Mockito.verify(productRepository).findById(nonExistentId);
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExist(){
            ProductDTO response = service.update(existingId, new ProductDTO(product));

            Assertions.assertNotNull(response);
            Mockito.verify(productRepository, times(1)).getOne(existingId);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
           Assertions.assertThrows(ResourcesNotFoundException.class, () -> {
               ProductDTO response = service.update(nonExistentId, new ProductDTO(product));
           });

           Mockito.verify(productRepository, times(1)).getOne(nonExistentId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(() -> service.delete(existingId));

        //verifica se foi feita alguma chamada expecifica ao Mock(repository)
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourcesNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourcesNotFoundException.class, () -> service.delete(nonExistentId));

        Mockito.verify(productRepository).deleteById(nonExistentId);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId(){
        Assertions.assertThrows(DataBaseException.class, () -> service.delete(dependentId));

        Mockito.verify(productRepository).deleteById(dependentId);
    }
}
