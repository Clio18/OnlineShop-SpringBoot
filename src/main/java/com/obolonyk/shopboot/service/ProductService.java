package com.obolonyk.shopboot.service;

import com.obolonyk.shopboot.entity.Product;
import com.obolonyk.shopboot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Repository
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product getById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Product with id %s not found", id)));
    }

    public void save(Product product) {
        LocalDateTime date = LocalDateTime.now();
        product.setCreationDate(date);
        repository.save(product);
    }

    public void remove(Integer id) {
        repository.deleteById(id);
    }


    public List<Product> getBySearch(String search) {
        return repository.findProductByNameOrDescriptionContains(search, search);
    }
}
