package com.store.demo.service;

import com.store.demo.DTO.OutputProductDTO;
import com.store.demo.domain.Product;

import java.sql.SQLException;
import java.util.List;

public interface UserService {

    OutputProductDTO getById(int id);

    List<OutputProductDTO> getByType(String type, int p);

    String addOrder(String productName);

    List<OutputProductDTO> showOrders(int page);

    List<OutputProductDTO> findAllPage(int page);

    Product recover(SQLException sqlException, int page);
}
