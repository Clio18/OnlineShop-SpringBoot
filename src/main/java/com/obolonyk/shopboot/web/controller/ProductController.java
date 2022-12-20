package com.obolonyk.shopboot.web.controller;

import com.obolonyk.shopboot.dto.ProductDto;
import com.obolonyk.shopboot.dto.mapper.EntityMapper;
import com.obolonyk.shopboot.entity.Product;
import com.obolonyk.shopboot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    private final ProductService productService;

    @Autowired
    private EntityMapper mapper;

    //URL: GET http://localhost:8080/api/v1/products
    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected @ResponseBody
    List<Product> getAllProducts() {

        return productService.getAll();
    }

    //URL: POST http://localhost:8080/api/v1/products
    @PostMapping()
    @PreAuthorize("hasAuthority('product:write')")
    protected ResponseEntity<Product> addProduct(@RequestBody ProductDto productDTO) {

        Product product = mapper.dtoToEntity(productDTO);
        productService.save(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    //URL: PUT http://localhost:8080/api/v1/products/...
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('product:write')")
    protected ResponseEntity<Product> updateProduct(@PathVariable Integer id,
                                                    @RequestBody ProductDto productDTO) {

        Product product = mapper.dtoToEntity(productDTO);
        product.setId(id);
        productService.save(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    //URL: POST http://localhost:8080/api/v1/products/...
    @PostMapping("{search}")
    @PreAuthorize("hasAuthority('product:read')")
    protected List<Product> searchProduct(@PathVariable String search) {

        return productService.getBySearch(search);
    }

    //URL: DELETE http://localhost:8080/api/v1/products/
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('product:write')")
    protected ResponseEntity<Product> deleteProduct(@PathVariable Integer id) {

        productService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
