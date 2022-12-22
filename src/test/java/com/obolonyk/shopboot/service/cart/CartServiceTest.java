package com.obolonyk.shopboot.service.cart;

import com.obolonyk.shopboot.entity.Product;
import com.obolonyk.shopboot.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    private final List<Order> emptyCart = new CopyOnWriteArrayList<>();

    @Mock
    private ProductService productService;

    private CartService cartService;
    private List<Order> cart;
    private Product product;
    private Order order;

    @BeforeEach
    void init() {
        product = Product.builder()
                .id(1)
                .name("Antman")
                .description("Antman is a superhero, based on the Marvel Comics superhero of the same name")
                .price(100.0)
                .creationDate(LocalDateTime.of(2022, 2, 24, 4, 0, 0))
                .build();
        order = Order.builder()
                .product(product)
                .quantity(1)
                .total(100.0)
                .build();
        cart = new CopyOnWriteArrayList<>();
        cart.add(order);
        cartService = new CartService(productService);
    }

    @Test
    @DisplayName("Test AddChosenProductToCart And Check That Cart Is NotEmpty, Size And Value")
    void testAddChosenProductToCart_AndCheckNotEmpty_Size_Value() {
        when(productService.getById(1)).thenReturn(product);
        cartService.addChosenProductToCart(emptyCart, 1);

        assertFalse(emptyCart.isEmpty());
        assertEquals(1, emptyCart.size());
        assertEquals(order, emptyCart.get(0));
    }

    @Test
    @DisplayName("Test IncreasingByOneInCart And Check The Size, Quantity and Total")
    void testIncreasingByOneInCart_AndCheckSize_Quantity_Total() {
        cartService.increasingByOneInCart(cart, 1);
        Order orderInCart = cart.get(0);
        Double price = product.getPrice() + product.getPrice();

        assertEquals(1, cart.size());
        assertEquals(2, orderInCart.getQuantity());
        assertEquals(price, orderInCart.getTotal());
    }

    @Test
    @DisplayName("Test DecreasingByOneCart And Check Is Empty")
    void testDecreasingByOneCart_AndCheckIsEmpty() {
        cartService.decreasingByOneCart(cart, 1);
        assertTrue(cart.isEmpty());
    }

    @Test
    @DisplayName("Test DecreasingByOneCart And Check Is Empty")
    void testDeleteChosenProductFromCart_AndCheckIsEmpty() {
        cartService.deleteChosenProductFromCart(cart, 1);
        assertTrue(cart.isEmpty());
    }

    @Test
    @DisplayName("Test AddChosenProductToCart When ProductId Doesn't Exist And Check Exception")
    void testIncreasingByOneInCart_WhenWrongId_AndCheckException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.increasingByOneInCart(emptyCart, 100);
        });
        assertEquals("No order found by provided id 100", exception.getMessage());
    }

    @Test
    @DisplayName("Test DecreasingByOneCart When ProductId Doesn't Exist And Check Exception")
    void testDecreasingByOneCart_WhenWrongId_AndCheckException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.decreasingByOneCart(emptyCart, 100);
        });
        assertEquals("No order found by provided id 100", exception.getMessage());
    }

    @Test
    @DisplayName("Test DeleteChosenProductFromCart When ProductId Doesn't Exist And Check Exception")
    void testDeleteChosenProductFromCart_WhenWrongId_AndCheckException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.deleteChosenProductFromCart(emptyCart, 100);
        });
        assertEquals("No order found by provided id 100", exception.getMessage());
    }

}