package com.store.demo.service;

import com.store.demo.DTO.OutputProductDTO;
import com.store.demo.domain.Product;
import com.store.demo.domain.TypeOfProduct;
import com.store.demo.domain.User;
import com.store.demo.repository.ProductRepo;
import com.store.demo.repository.TypeRepo;
import com.store.demo.repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private ProductRepo productRepo;

    @MockBean
    private TypeRepo typeRepo;

    @MockBean
    private UserRepo userRepo;

    @Test
    void getByType() {
        TypeOfProduct type=new TypeOfProduct();
        type.setType("type");

        Product product1=new Product();
        product1.setPrice(100);
        product1.setName("name");
        product1.setType(type);

        Product product2=new Product();
        product2.setPrice(200);
        product2.setName("main");
        product2.setType(type);

        List<Product> products=new ArrayList<>();
        products.add(product1);
        products.add(product2);

        Mockito.doReturn(type)
                .when(typeRepo)
                .findByType("type");

        Mockito.when(productRepo.findAllByType(type))
                .thenReturn(products);

        List<OutputProductDTO> output = userService.getByType("type", 0);

        Assertions.assertEquals(1, output.size());
    }

    @Test
    @WithMockUser
    void addOrder() {
        TypeOfProduct type=new TypeOfProduct();
        type.setType("type");

        Product product=new Product();
        product.setPrice(100);
        product.setName("name");
        product.setType(type);

        User user=new User();
        user.setUsername("user");
        user.setProducts(new ArrayList<>());

        Mockito.doReturn(product)
                .when(productRepo)
                .findByName("name");

        Mockito.when(userRepo.findByUsername(any()))
                .thenReturn(user);

        userService.addOrder("name");

        Assertions.assertEquals(1, user.getProducts().size());
    }
}