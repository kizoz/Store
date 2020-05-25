package com.store.demo.integrationTest;


import com.store.demo.controller.AdminProductController;
import com.store.demo.controller.UserProductController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private AdminProductController adminProductController;

    @Autowired
    private UserProductController userProductController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void contextTest(){
        assertNotNull(adminProductController);
    }

    @WithMockUser("u")
    @Test
    public void getByIdTest() throws Exception{
        
    }

}
