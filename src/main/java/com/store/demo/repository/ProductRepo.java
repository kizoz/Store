package com.store.demo.repository;

import com.store.demo.domain.Product;
import com.store.demo.domain.TypeOfProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Integer> {

    List<Product> findAllByType(TypeOfProduct type);

    Product findByName(String name);

    Page<Product> findAll(Pageable pageable);
}
