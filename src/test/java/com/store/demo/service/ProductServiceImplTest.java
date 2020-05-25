package com.store.demo.service;

import com.store.demo.DTO.AddProductDTO;
import com.store.demo.DTO.AddTypeDTO;
import com.store.demo.DTO.EditProductDTO;
import com.store.demo.DTO.OutputProductDTO;
import com.store.demo.domain.Product;
import com.store.demo.domain.TypeOfProduct;
import com.store.demo.repository.ProductRepo;
import com.store.demo.repository.TypeRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.retry.ExhaustedRetryException;

@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private ProductRepo productRepo;

    @MockBean
    private TypeRepo typeRepo;

    @Test
    void testMapper(){
        Product product=new Product();
        TypeOfProduct typeOfProduct= new TypeOfProduct();
        typeOfProduct.setType("TYPE");
        product.setPrice(100);
        product.setName("NAME");
        product.setType(typeOfProduct);

        OutputProductDTO outputProductDTO= modelMapper.map(product, OutputProductDTO.class);

        Assertions.assertEquals(product.getName(), outputProductDTO.getName());
        Assertions.assertEquals(product.getPrice(), outputProductDTO.getPrice());
        Assertions.assertEquals(product.getType().getType(), outputProductDTO.getType());
    }

    @Test
    void addType() {
        AddTypeDTO addTypeDTO= new AddTypeDTO();
        addTypeDTO.setType("TYPE");

        TypeOfProduct type=new TypeOfProduct();
        type.setType("TYPE");

        String actual = productService.addType(addTypeDTO);

        Assertions.assertEquals(type.toString(), actual);
    }

    @Test
    void addTypeError(){
        AddTypeDTO type1 = new AddTypeDTO();
        type1.setType("TYPE");

        Mockito.when(typeRepo.findByType("TYPE"))
                .thenReturn(new TypeOfProduct());

        Assertions.assertThrows(ExhaustedRetryException.class, () -> productService.addType(type1));
    }

    @Test
    void addProduct() {
        AddProductDTO addProductDTO = new AddProductDTO();
        addProductDTO.setName("name");
        addProductDTO.setPrice(100);
        addProductDTO.setType("type");

        TypeOfProduct type = new TypeOfProduct();
        type.setType("type");

        Mockito.when(typeRepo.findByType("type"))
                    .thenReturn(type);

        String actual = productService.addProduct(addProductDTO);

        Assertions.assertEquals("Product {name='name', price=100, type='type'}", actual);
    }

    @Test
    void edit(){
        TypeOfProduct type = new TypeOfProduct();
        type.setType("type");

        TypeOfProduct typeE = new TypeOfProduct();
        typeE.setType("e");

        Product product=new Product();
        product.setId(1);
        product.setType(type);
        product.setName("name");
        product.setPrice(100);

        EditProductDTO editProductDTO = new EditProductDTO();
        editProductDTO.setId(1);
        editProductDTO.setName("phone");
        editProductDTO.setPrice(null);
        editProductDTO.setType("e");

        Mockito.when(productRepo.getOne(1))
                .thenReturn(product);

        Mockito.when(productRepo.existsById(1))
                .thenReturn(true);

        Mockito.when(typeRepo.findByType("e"))
                .thenReturn(typeE);

        String actual = productService.editProduct(editProductDTO);

        String expected = "Product with id= 1 was updated to Product {name='phone', price=100, type='e'}";

        Assertions.assertEquals(expected,actual);
    }
}