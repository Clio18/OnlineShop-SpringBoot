package com.obolonyk.shopboot.web.controller;

import com.obolonyk.shopboot.entity.Order;

import com.obolonyk.shopboot.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@SessionAttributes("cart")
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/cart", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartController {

    private final CartService cartService;

    @ModelAttribute("cart")
    public List<Order> cart() {
        return new CopyOnWriteArrayList<>();
    }

    @PostMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected void addToCart(@PathVariable Integer id,
                                   @ModelAttribute("cart") List<Order> cart,
                                   RedirectAttributes attributes) {

        List<Order> cartUpdated = cartService.addChosenProductToCart(id, cart);
        attributes.addFlashAttribute("cart", cartUpdated);
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected List<Order> getCart(@ModelAttribute("cart") List<Order> cart) {

        return cart;
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected void deleteFromCart(@PathVariable Integer id,
                                        @ModelAttribute("cart") List<Order> cart,
                                        RedirectAttributes attributes) {

        cartService.deleteChosenProductFromCart(cart, id);
        attributes.addFlashAttribute("cart", cart);
    }

    @PostMapping(path = "minus/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected void updateCartMinus(@PathVariable Integer id,
                                         @ModelAttribute("cart") List<Order> cart,
                                         RedirectAttributes attributes) {

        cartService.decreasingByOneCart(cart, id);
        attributes.addFlashAttribute("cart", cart);
    }

    @PostMapping(path = "plus/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected void updateCartPlus(@PathVariable Integer id,
                                        @ModelAttribute("cart") List<Order> cart,
                                        RedirectAttributes attributes) {

        cartService.increasingByOneInCart(cart, id);
        attributes.addFlashAttribute("cart", cart);
    }
}
