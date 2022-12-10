package com.obolonyk.shopboot.web.controller;

import com.obolonyk.shopboot.entity.Product;
import com.obolonyk.shopboot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    private final ProductService productService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected @ResponseBody
    List<Product> getAllProducts() {

        return productService.getAll();
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('product:write')")
    protected ResponseEntity<Product> addProduct(@RequestBody Product product) {

        product.setCreationDate(LocalDateTime.now());
        productService.save(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('product:write')")
    protected ResponseEntity<Product> updateProduct(@PathVariable Integer id,
                                                    @RequestBody Product product) {

        product.setId(id);
        product.setCreationDate(LocalDateTime.now());
        productService.save(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping("{search}")
    @PreAuthorize("hasAuthority('product:read')")
    protected List<Product> searchProduct(@PathVariable String search) {

        return productService.getBySearch(search);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('product:write')")
    protected ResponseEntity<Product> deleteProduct(@PathVariable Integer id) {

        productService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
