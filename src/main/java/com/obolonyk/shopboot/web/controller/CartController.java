package com.obolonyk.shopboot.web.controller;

import com.obolonyk.shopboot.entity.Order;

import com.obolonyk.shopboot.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
@SessionAttributes("cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @ModelAttribute("cart")
    public List<Order> cart() {
        return new CopyOnWriteArrayList<>();
    }

    @PostMapping(path = "/product/cart")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected String addToCart(@RequestParam Integer id,
                                   @ModelAttribute("cart") List<Order> cart,
                                   RedirectAttributes attributes) {

        List<Order> cartUpdated = cartService.addChosenProductToCart(id, cart);
        attributes.addFlashAttribute("cart", cartUpdated);
        return "redirect:/products";
    }

    @GetMapping(path = "/products/cart")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected String getCart(ModelMap model, @ModelAttribute("cart") List<Order> cart) {

        double totalPrice = cartService.getTotalProductsPrice(cart);
        model.addAttribute("orders", cart);
        model.addAttribute("totalPrice", totalPrice);
        return "cart";
    }

    @PostMapping(path = "/products/cart/delete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected String deleteFromCart(@RequestParam Integer id,
                                        @ModelAttribute("cart") List<Order> cart,
                                        RedirectAttributes attributes) {

        cartService.deleteChosenProductFromCart(cart, id);
        attributes.addFlashAttribute("cart", cart);
        return "redirect:/products/cart";
    }

    @PostMapping(path = "/products/cart/update/minus")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected String updateCartMinus(@RequestParam Integer id,
                                         @ModelAttribute("cart") List<Order> cart,
                                         RedirectAttributes attributes) {

        cartService.decreasingByOneCart(cart, id);
        attributes.addFlashAttribute("cart", cart);
        return "redirect:/products/cart";
    }

    @PostMapping(path = "/products/cart/update/plus")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected String updateCartPlus(@RequestParam Integer id,
                                        @ModelAttribute("cart") List<Order> cart,
                                        RedirectAttributes attributes) {

        cartService.increasingByOneInCart(cart, id);
        attributes.addFlashAttribute("cart", cart);
        return "redirect:/products/cart";
    }
}
