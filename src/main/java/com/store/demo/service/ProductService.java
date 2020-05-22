package com.store.demo.service;

import com.store.demo.DTO.AddProductDTO;
import com.store.demo.DTO.AddTypeDTO;
import com.store.demo.DTO.EditProductDTO;
import com.store.demo.domain.User;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {

    String addProduct(AddProductDTO productDTO);

    String editProduct(EditProductDTO productDTO);

    String deleteById(int id);

    String addType(AddTypeDTO typeDTO);

    List<User> showCustomers(String productName, int page);

    String recover(SQLException sqlException, int id);

}
