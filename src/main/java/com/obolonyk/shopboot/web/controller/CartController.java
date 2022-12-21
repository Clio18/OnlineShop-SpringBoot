package com.obolonyk.shopboot.web.controller;

import com.obolonyk.shopboot.service.cart.Order;

import com.obolonyk.shopboot.service.cart.CartService;
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

    //URL: POST http://localhost:8080/api/v1/cart?product_id=...
    @PostMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected void addToCart(@RequestParam("product_id") Integer id,
                             @ModelAttribute("cart") List<Order> cart,
                             RedirectAttributes attributes) {

        List<Order> cartUpdated = cartService.addChosenProductToCart(cart, id);
        attributes.addFlashAttribute("cart", cartUpdated);
    }

    //URL: GET http://localhost:8080/api/v1/cart
    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected List<Order> getCart(@ModelAttribute("cart") List<Order> cart) {

        return cart;
    }

    //URL: DELETE http://localhost:8080/api/v1/cart?product_id=...
    @DeleteMapping()
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected void deleteFromCart(@RequestParam("product_id") Integer id,
                                  @ModelAttribute("cart") List<Order> cart,
                                  RedirectAttributes attributes) {

        cartService.deleteChosenProductFromCart(cart, id);
        attributes.addFlashAttribute("cart", cart);
    }

    //URL: POST http://localhost:8080/api/v1/cart/minus?product_id=...
    //For those products which are available in cart
    @PostMapping(path = "minus")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected void updateCartMinus(@RequestParam("product_id") Integer id,
                                   @ModelAttribute("cart") List<Order> cart,
                                   RedirectAttributes attributes) {

        cartService.decreasingByOneCart(cart, id);
        attributes.addFlashAttribute("cart", cart);
    }

    //URL: POST http://localhost:8080/api/v1/cart/plus?product_id=...
    //For those products which are available in cart
    @PostMapping(path = "plus")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected void updateCartPlus(@RequestParam("product_id") Integer id,
                                  @ModelAttribute("cart") List<Order> cart,
                                  RedirectAttributes attributes) {

        cartService.increasingByOneInCart(cart, id);
        attributes.addFlashAttribute("cart", cart);
    }
}
