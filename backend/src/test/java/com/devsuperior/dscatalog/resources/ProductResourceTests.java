package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourcesNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ProductService service;

    private long existingId;
    private long nonExistingId;
    private long dependentid;

    private PageImpl<ProductDTO> page;
    private ProductDTO productDTO;


    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO(Factory.createProduct());
        page = new PageImpl<>(List.of(productDTO));
        existingId = 1L;
        nonExistingId = 2L;
        dependentid = 3L;

        when(service.findAll(any())).thenReturn(page);

        when(service.findById(existingId)).thenReturn(productDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourcesNotFoundException.class);

        when(service.insert(any())).thenReturn(productDTO);

        when(service.update(eq(existingId), any())).thenReturn(productDTO);
        when(service.update(eq(nonExistingId), any())).thenThrow(ResourcesNotFoundException.class);

        doNothing().when(service).delete(existingId);
        doThrow(ResourcesNotFoundException.class).when(service).delete(nonExistingId);
        doThrow(DataBaseException.class).when(service).delete(dependentid);

    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/products")
                //req vai aceitar JSON como resposta
                .accept(MediaType.APPLICATION_JSON)
        );

        //codHttp = 200
        response.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk());
        //verifica se 'id'existe no obj retornado na req
        response.andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenDoesNotExistsId() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnProductDTO() throws Exception {
        String productDtoJson = objectMapper.writeValueAsString(productDTO);

        ResultActions response =
                mockMvc.perform(post("/products")
                        .content(productDtoJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                );

        response.andExpect(status().isCreated());
        response.andExpect(jsonPath("$.id").exists());
        response.andExpect(jsonPath("$.name").exists());
        response.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        //converte obj, para string
        String productDtoJson = objectMapper.writeValueAsString(productDTO);

        ResultActions response =
                mockMvc.perform(put("/products/{id}", existingId)
                .content(productDtoJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.id").exists());
        response.andExpect(jsonPath("$.name").exists());
        response.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void deleteShouldDeleteProductWhenIdExists() throws Exception {
        ResultActions response =
                mockMvc.perform(delete("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON)
                );

        response.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenDoesNotExistsId() throws Exception {
        ResultActions response =
                mockMvc.perform(delete("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON)
                );

        response.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenDependentId() throws Exception{
        ResultActions response =
                mockMvc.perform(put("/products/{id}", dependentid)
                        .accept(MediaType.APPLICATION_JSON)
                );

        response.andExpect(status().isBadRequest());
    }
}
