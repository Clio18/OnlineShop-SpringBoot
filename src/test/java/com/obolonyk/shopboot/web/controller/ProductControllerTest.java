//package com.obolonyk.shopboot.web.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.obolonyk.shopboot.entity.Product;
//import com.obolonyk.shopboot.service.ProductService;
//import com.obolonyk.shopboot.web.SpringSecurityTestConfig;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
//        classes = SpringSecurityTestConfig.class)
//@AutoConfigureMockMvc
//class ProductControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private ProductService productService;
//
//    @Test
//    @WithUserDetails("admin")
//    void testGetListOfProducts() throws Exception {
//        Product product1 = Product.builder()
//                .id(1)
//                .name("Antman")
//                .description("Antman is a superhero, based on the Marvel Comics superhero of the same name")
//                .price(100.0)
//                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
//                .build();
//        Product product2 = Product.builder()
//                .id(2)
//                .name("Batman")
//                .description("Help Batman stop crime and save civilians with Batman toys")
//                .price(200.0)
//                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
//                .build();
//        Product product3 = Product.builder()
//                .id(3)
//                .name("Sega")
//                .description("A Japanese video game developer and publisher and manufacturer of arcade games and formerly of video game consoles.")
//                .price(99.0)
//                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
//                .build();
//
//        List<Product> productsList = new ArrayList<>();
//        productsList.add(product1);
//        productsList.add(product2);
//        productsList.add(product3);
//
//        when(productService.getAll()).thenReturn(productsList);
//        mockMvc.perform( MockMvcRequestBuilders
//                .get("/api/v1/products/")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].name").value("Antman"))
//                .andExpect(jsonPath("$[0].price").value(100.0))
//                .andExpect(jsonPath("$[1].name").value("Batman"))
//                .andExpect(jsonPath("$[1].price").value(200.0))
//                .andExpect(jsonPath("$[2].name").value("Sega"))
//                .andExpect(jsonPath("$[2].price").value(99.0));
//        verify(productService).getAll();
//    }
//
//}