package com.obolonyk.shopboot.service.cart;

import com.obolonyk.shopboot.entity.Product;
import com.obolonyk.shopboot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductService productService;

    public List<Order> addChosenProductToCart(List<Order> cart, Integer productId) {
        Product product = productService.getById(productId);

        for (Order order : cart) {
            if (order.getProduct().getName().equals(product.getName())) {
                int quantity = order.getQuantity() + 1;
                double total = quantity * product.getPrice();
                order.setQuantity(quantity);
                order.setTotal(total);
                return cart;
            }
        }
        Order order = Order.builder()
                .product(product)
                .quantity(1)
                .total(product.getPrice())
                .build();
        cart.add(order);
        return cart;
    }

    public void decreasingByOneCart(List<Order> cart, Integer id) {
        CartAction.REMOVE_FROM_CART.perform(cart, id);
    }

    public void increasingByOneInCart(List<Order> cart, Integer id) {
        CartAction.ADD_TO_CART.perform(cart, id);
    }

    public void deleteChosenProductFromCart(List<Order> cart, Integer id) {
        CartAction.DELETE.perform(cart, id);
    }
}
