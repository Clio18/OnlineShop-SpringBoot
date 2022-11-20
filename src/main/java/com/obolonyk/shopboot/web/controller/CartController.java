package com.obolonyk.shopboot.web.controller;

import com.obolonyk.shopboot.entity.Order;
import com.obolonyk.shopboot.entity.Product;

import com.obolonyk.shopboot.service.CartService;
import com.obolonyk.shopboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
@SessionAttributes("cart")
public class CartController {
    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public CartController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @ModelAttribute("cart")
    public List<Order> cart() {
        return new CopyOnWriteArrayList<>();
    }

    @PostMapping(path = "/product/cart")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected String addToCartPost(@RequestParam Integer id,
                                   @ModelAttribute("cart") List<Order> cart,
                                   RedirectAttributes attributes) {

        Optional<Product> optionalProduct = productService.getById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            cartService.addChosenProductToCart(product, cart);
        }
        attributes.addFlashAttribute("cart", cart);
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
