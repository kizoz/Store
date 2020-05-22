package com.store.demo.service;

import com.store.demo.domain.TypeOfProduct;
import com.store.demo.repository.ProductRepo;
import com.store.demo.repository.TypeRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.retry.ExhaustedRetryException;

@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepo productRepo;
    @MockBean
    private TypeRepo typeRepo;

    /*@Test
    void addType() {
        TypeOfProduct type1 = new TypeOfProduct();
        type1.setType("TYPE");

        String actual = productService.addType(type1);

        Assertions.assertEquals(type1.toString(), actual);
        Mockito.verify(typeRepo, Mockito.times(1)).save(type1);
        Mockito.verify(typeRepo, Mockito.times(1)).findByType(type1.getType());
    }

    @Test
    void addTypeError(){
        TypeOfProduct type1 = new TypeOfProduct();
        type1.setType("TYPE");

        Mockito.doReturn(new TypeOfProduct())
                .when(typeRepo)
                .findByType("TYPE");

        Assertions.assertThrows(ExhaustedRetryException.class, () -> productService.addType(type1));
    }

    @Test
    void addProduct() {
    }*/
}