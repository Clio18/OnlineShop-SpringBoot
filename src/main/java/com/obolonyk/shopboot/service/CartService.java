package com.obolonyk.shopboot.service;

import com.obolonyk.shopboot.entity.Order;
import com.obolonyk.shopboot.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final ProductService productService;

    @Autowired
    public CartService(ProductService productService) {
        this.productService = productService;
    }

    public int getTotalProductsCount(List<Order> cart) {
        int count = 0;
        if (cart != null) {
            for (Order order : cart) {
                count = count + order.getQuantity();
            }
        }
        return count;
    }

    public double getTotalProductsPrice(List<Order> orders) {
        double totalPrice = 0;
        if (orders==null){
            return totalPrice;
        }
        for (Order order : orders) {
            totalPrice = totalPrice + (order.getQuantity() * order.getProduct().getPrice());
        }
        return totalPrice;
    }

    public List<Order> addChosenProductToCart(Integer productId, List<Order> cart) {
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
