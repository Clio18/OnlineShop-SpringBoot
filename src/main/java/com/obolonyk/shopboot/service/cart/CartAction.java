package com.obolonyk.shopboot.service.cart;

import java.util.List;

public enum CartAction {

    ADD_TO_CART {
        @Override
        public void perform(List<Order> cart, long id) {
            Order order = findOrderInCartById(cart, id);

            int newQuantity = order.getQuantity() + 1;
            double newTotal = order.getTotal() + order.getProduct().getPrice();

            order.setQuantity(newQuantity);
            order.setTotal(newTotal);
        }
    },

    REMOVE_FROM_CART {
        @Override
        public void perform(List<Order> cart, long id) {
            Order order = findOrderInCartById(cart, id);

            int newQuantity = order.getQuantity() - 1;

            if (newQuantity != 0) {
                double newTotal = order.getTotal() - order.getProduct().getPrice();
                order.setQuantity(newQuantity);
                order.setTotal(newTotal);
                return;
            }
            cart.remove(order);
        }
    },

    DELETE {
        @Override
        public void perform(List<Order> cart, long id) {
            Order order = findOrderInCartById(cart, id);
            cart.remove(order);
        }
    };

    public abstract void perform(List<Order> cart, long id);

    static Order findOrderInCartById(List<Order> cart, long id) {
        return cart.stream()
                .filter(order -> order.getProduct().getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No order found by provided id " + id));
    }

}
