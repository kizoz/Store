package com.store.demo.service;

import com.store.demo.domain.User;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {

    String addProduct(String name, Integer price, String type);

    String editProduct(Integer id, String name, int price, String type);

    String deleteById(int id);

    String addType(String type);

    List<User> showCustomers(String productName, int page);

    String recover(SQLException sqlException, int id);

}
