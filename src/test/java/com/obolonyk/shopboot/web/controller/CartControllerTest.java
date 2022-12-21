package com.obolonyk.shopboot.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obolonyk.shopboot.entity.Product;
import com.obolonyk.shopboot.service.cart.CartService;
import com.obolonyk.shopboot.service.cart.Order;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class)
@AutoConfigureMockMvc
class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    private Order order;

    @BeforeEach
    void init(){
        Product product = Product.builder()
                .id(1)
                .name("Antman")
                .description("Antman is a superhero, based on the Marvel Comics superhero of the same name")
                .price(100.0)
                .creationDate(LocalDateTime.of(2022, 2,24, 4, 0, 0))
                .build();
        order = Order.builder()
                .product(product)
                .quantity(1)
                .total(100.0)
                .build();
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("Test GetListOfProducts And Check Status Code")
    void testGetLCart_AndCheckStatus() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                .get("/api/v1/cart/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("Test AddChosenProductToCart And Check Status Code And Service Method Calling")
    void testAddChosenProductToCart_AndCheckStatus_ServiceMethodCalling() throws Exception {
        List<Order> cart = new CopyOnWriteArrayList<>();
        List<Order> updatedCart = new CopyOnWriteArrayList<>();
        updatedCart.add(order);

        when(cartService.addChosenProductToCart(cart, 1)).thenReturn(updatedCart);
        mockMvc.perform( MockMvcRequestBuilders
                .post("/api/v1/cart?product_id=1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cartService).addChosenProductToCart(cart, 1);
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("Test DeleteChosenProductFromCart And Check Status Code And Service Method Calling")
    void testDeleteChosenProductFromCart_AndCheckStatus_ServiceMethodCalling() throws Exception {
        List<Order> cart = new CopyOnWriteArrayList<>();

        doNothing().when(cartService).deleteChosenProductFromCart(cart, 1);

        mockMvc.perform( MockMvcRequestBuilders
                .delete("/api/v1/cart?product_id=1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cartService).deleteChosenProductFromCart(cart, 1);
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("Test UpdateCartMinus And Check Status Code And Service Method Calling")
    void testUpdateCartMinus_AndCheckStatus_ServiceMethodCalling() throws Exception {
        List<Order> cart = new CopyOnWriteArrayList<>();

        doNothing().when(cartService).decreasingByOneCart(cart, 1);

        mockMvc.perform( MockMvcRequestBuilders
                .post("/api/v1/cart/minus?product_id=1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cartService).decreasingByOneCart(cart, 1);
    }

    @Test
    @WithUserDetails("admin")
    @DisplayName("Test UpdateCartPlus And Check Status Code And Service Method Calling")
    void testUpdateCartPlus_AndCheckStatus_ServiceMethodCalling() throws Exception {
        List<Order> cart = new CopyOnWriteArrayList<>();

        doNothing().when(cartService).increasingByOneInCart(cart, 1);

        mockMvc.perform( MockMvcRequestBuilders
                .post("/api/v1/cart/plus?product_id=1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cartService).increasingByOneInCart(cart, 1);
    }

}