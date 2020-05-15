package com.store.demo.service;

import com.store.demo.domain.Product;
import org.springframework.data.domain.Page;

import java.sql.SQLException;
import java.util.List;

public interface UserService {

    Product getById(int id);

    List<Product> getByType(String type, int p);

    void addOrder(String productName);

    List<Product> showOrders(int page);

    Page<Product> findAllPage(int page);

    Product recover(SQLException sqlException, int page);
}
