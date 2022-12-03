package com.obolonyk.shopboot.web.controller;

import com.obolonyk.shopboot.entity.Order;

import com.obolonyk.shopboot.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
@SessionAttributes("cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @ModelAttribute("cart")
    public List<Order> cart() {
        return new CopyOnWriteArrayList<>();
    }

    @PostMapping(path = "/product/cart")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected String addToCartPost(@RequestParam Long id,
                                   @ModelAttribute("cart") List<Order> cart,
                                   RedirectAttributes attributes) {

        List<Order> cartUpdated = cartService.addChosenProductToCart(id, cart);
        attributes.addFlashAttribute("cart", cartUpdated);
        return "redirect:/products";
    }

    @GetMapping(path = "/products/cart")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected String getCartGet(ModelMap model, @ModelAttribute("cart") List<Order> cart) {

        double totalPrice = cartService.getTotalProductsPrice(cart);
        model.addAttribute("orders", cart);
        model.addAttribute("totalPrice", totalPrice);
        return "cart";
    }

    @PostMapping(path = "/products/cart/delete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected String deleteFromCartPost(@RequestParam Long id,
                                        @ModelAttribute("cart") List<Order> cart,
                                        RedirectAttributes attributes) {

        cartService.deleteChosenProductFromCart(cart, id);
        attributes.addFlashAttribute("cart", cart);
        return "redirect:/products/cart";
    }

    @PostMapping(path = "/products/cart/update/minus")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected String updateCartMinusPost(@RequestParam Long id,
                                         @ModelAttribute("cart") List<Order> cart,
                                         RedirectAttributes attributes) {

        cartService.decreasingByOneCart(cart, id);
        attributes.addFlashAttribute("cart", cart);
        return "redirect:/products/cart";
    }

    @PostMapping(path = "/products/cart/update/plus")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected String updateCartPlusPost(@RequestParam Long id,
                                        @ModelAttribute("cart") List<Order> cart,
                                        RedirectAttributes attributes) {

        cartService.increasingByOneInCart(cart, id);
        attributes.addFlashAttribute("cart", cart);
        return "redirect:/products/cart";
    }
}
