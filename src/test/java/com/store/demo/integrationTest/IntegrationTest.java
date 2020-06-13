package com.store.demo.integrationTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.demo.DTO.AddProductDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class IntegrationTest {

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void containerTest() throws Exception{
        Assertions.assertTrue(mySQLContainer.isRunning());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void addProductTest() throws Exception{

        AddProductDTO addProductDTO = new AddProductDTO();
        addProductDTO.setName("card");
        addProductDTO.setPrice(100);
        addProductDTO.setType("comp");

        mockMvc.perform(post("/adm/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(addProductDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Saved Product {name='card', price=100, type='comp'}")));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void allTest() throws Exception{

        mockMvc.perform(get("/all/0"))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$", iterableWithSize(5)));
    }
}
