package com.obolonyk.shopboot.service;

import com.obolonyk.shopboot.entity.Product;
import com.obolonyk.shopboot.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepositoryMock;

    private ProductService productService;
    private List<Product> products;
    private Product product1;
    private Product product2;

    @BeforeEach
    void init() {
        productService = new ProductService(productRepositoryMock);
        products = new ArrayList<>();
        product1 = Product.builder()
                .id(1)
                .name("Antman")
                .description("Antman is a superhero, based on the Marvel Comics superhero of the same name")
                .price(100.0)
                .creationDate(LocalDateTime.of(2022, 2, 24, 4, 0, 0))
                .build();
        product2 = Product.builder()
                .id(2)
                .name("Batman")
                .description("Help Batman stop crime and save civilians with Batman toys")
                .price(200.0)
                .creationDate(LocalDateTime.of(2022, 2, 24, 4, 0, 0))
                .build();
        products.add(product1);
        products.add(product2);
    }

    @Test
    @DisplayName("test getAll and check result is not null, size, content equality, calling the repo's method")
    void testGetAll_AndCheckResultNotNull_Size_Content_CallingTheRepoMethod() {
        when(productRepositoryMock.findAll()).thenReturn(products);
        List<Product> actualProducts = productService.getAll();
        assertNotNull(actualProducts);
        assertEquals(2, actualProducts.size());
        assertEquals(product1, actualProducts.get(0));
        assertEquals(product2, actualProducts.get(1));
        verify(productRepositoryMock).findAll();
    }

    @Test
    @DisplayName("test findById and check content equality and calling the repo's method")
    void testFindById_AndCheckContent_CallingTheRepoMethod() {
        when(productRepositoryMock.findById(1)).thenReturn(Optional.ofNullable(products.get(0)));
        Product actualProduct = productService.getById(1);
        assertEquals(product1, actualProduct);
        verify(productRepositoryMock).findById(1);
    }

    @Test
    @DisplayName("test add and check calling the repo's method")
    void testAdd_AndCallingTheRepoMethod() {
        Product product3 = Product.builder()
                .id(3)
                .name("Yoyo")
                .description("A yo-yo (also spelled yoyo) is a toy consisting of an axle connected to two disks, and a string looped around the axle, similar to a spool.")
                .price(10.0)
                .creationDate(LocalDateTime.of(2022, 2, 24, 4, 0, 0))
                .build();

        //interface repository's method save not void!
        //doNothing().when(productRepositoryMock).save(product3);
        productRepositoryMock.save(product3);
        verify(productRepositoryMock).save(product3);
    }

    @Test
    @DisplayName("test delete and check calling the repo's method")
    void testDelete_AndCallingTheRepoMethod() {
        doNothing().when(productRepositoryMock).deleteById(isA(Integer.class));
        productRepositoryMock.deleteById(1);
        verify(productRepositoryMock, times(1)).deleteById(1);
    }
}