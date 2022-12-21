package com.obolonyk.shopboot.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obolonyk.shopboot.entity.Product;
import com.obolonyk.shopboot.service.ProductService;
import com.obolonyk.shopboot.web.SpringSecurityTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class)
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void init(){
       product1 = Product.builder()
                .id(1)
                .name("Antman")
                .description("Antman is a superhero, based on the Marvel Comics superhero of the same name")
                .price(100.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        product2 = Product.builder()
                .id(2)
                .name("Batman")
                .description("Help Batman stop crime and save civilians with Batman toys")
                .price(200.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        product3 = Product.builder()
                .id(3)
                .name("Sega")
                .description("A Japanese video game developer and publisher and manufacturer of arcade games and formerly of video game consoles.")
                .price(99.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
    }

    // ===================================      ADMIN     ===================================
    @Test
    @WithUserDetails("admin")
    @DisplayName("Test GetListOfProducts And Check Status Code, Result Size, Fields, Service Method Calling")
    void testGetListOfProducts_AndCheckStatus_Size_Fields_ServiceMethodCalling() throws Exception {
        List<Product> productsList = new ArrayList<>();
        productsList.add(product1);
        productsList.add(product2);
        productsList.add(product3);

        when(productService.getAll()).thenReturn(productsList);
        mockMvc.perform( MockMvcRequestBuilders
                .get("/api/v1/products/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Antman"))
                .andExpect(jsonPath("$[0].price").value(100.0))
                .andExpect(jsonPath("$[1].name").value("Batman"))
                .andExpect(jsonPath("$[1].price").value(200.0))
                .andExpect(jsonPath("$[2].name").value("Sega"))
                .andExpect(jsonPath("$[2].price").value(99.0));
        verify(productService).getAll();
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("Test SaveProduct And Check Status Code, Fields, Service Method Calling")
    void testSaveProduct_AndCheckStatus_Fields_ServiceMethodCalling() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .post("/api/v1/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Antman"))
                .andExpect(jsonPath("$.price").value(100.0));
        verify(productService).save(any(Product.class));
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("Test UpdateProduct And Check Status Code, Fields, Service Method Calling")
    void testUpdateProduct_AndCheckStatus_Fields_ServiceMethodCalling() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .put("/api/v1/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Antman"))
                .andExpect(jsonPath("$.price").value(100.0));
        verify(productService).save(any(Product.class));
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("Test SearchProducts And Check Status Code, Result Size, Fields, Service Method Calling")
    void testSearchProducts_AndCheckStatus_Size_Fields_ServiceMethodCalling() throws Exception {
        List<Product> productsList = new ArrayList<>();
        productsList.add(product1);

        when(productService.getBySearch("Antman")).thenReturn(productsList);
        mockMvc.perform( MockMvcRequestBuilders
                .post("/api/v1/products/{search}", "Antman")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Antman"))
                .andExpect(jsonPath("$[0].price").value(100.0));

        verify(productService).getBySearch("Antman");
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("Test RemoveProduct And Check Status Code And Service Method Calling")
    void testRemoveProduct_AndCheckStatus_ServiceMethodCalling() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .delete("/api/v1/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isNoContent());
        verify(productService).remove(1);
    }

    // ===================================      USER     ===================================
    @Test
    @DisplayName("Test SaveProduct For Users And Check It Is Forbidden")
    @WithMockUser(roles = "USER")
    void testSaveProductForUsers_CheckForbidden() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .post("/api/v1/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Test UpdateProduct For Users And Check It Is Forbidden")
    void testUpdateProductForUsers_CheckForbidden() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .put("/api/v1/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Test RemoveProduct For Users And Check It Is Forbidden")
    void testRemoveProductForUsers_CheckForbidden() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .delete("/api/v1/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isForbidden());
    }

}