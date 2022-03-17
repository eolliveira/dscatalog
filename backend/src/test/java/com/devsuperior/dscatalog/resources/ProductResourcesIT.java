package com.devsuperior.dscatalog.resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourcesIT {

    @Autowired
    private MockMvc mockMvc;

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
    public void findAllShouldReturnPageSortedByName() throws Exception {
        ResultActions response =
                mockMvc.perform(MockMvcRequestBuilders.get("/products?page=0&size=5&sort=name,asc")
                        .accept(MediaType.APPLICATION_JSON)
                        );

                response.andExpect(status().isOk());
                response.andExpect(jsonPath("$.totalElements").value(totalDatabaseRecords));
                response.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
                response.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
                response.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
    }


}
