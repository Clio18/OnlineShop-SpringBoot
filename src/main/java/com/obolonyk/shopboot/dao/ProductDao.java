package com.obolonyk.shopboot.dao;

import com.obolonyk.shopboot.entity.Product;

import java.util.List;

public interface ProductDao {

    List<Product> getAll();

    Product getById(int id);

    void save(Product product);

    void remove(int id);

    void update(Product product);

    List<Product> getBySearch(String pattern);

}
