package com.store.demo.repository;

import com.store.demo.domain.TypeOfProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepo extends JpaRepository<TypeOfProduct, Integer> {
    TypeOfProduct findByType(String type);
}
