package com.obolonyk.shopboot.repository;

import com.obolonyk.shopboot.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findProductByNameOrDescriptionContains(String searchName, String searchDescription);
}

