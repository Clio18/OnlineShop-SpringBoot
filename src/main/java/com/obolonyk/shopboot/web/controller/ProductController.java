package com.obolonyk.shopboot.web.controller;

import com.obolonyk.shopboot.entity.Order;
import com.obolonyk.shopboot.entity.Product;
import com.obolonyk.shopboot.service.CartService;
import com.obolonyk.shopboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public ProductController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping(path = "/products")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected String getProducts(Model model,
                                 //ArrayList has a constructor
                                 @ModelAttribute("cart") ArrayList<Order> cart) {

        int count = cartService.getTotalProductsCount(cart);
        model.addAttribute("count", count);

        List<Product> products = productService.getAll();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping(path = "/products/add")
    @PreAuthorize("hasAuthority('product:write')")
    protected String addProductGet() {
        return "addProduct";
    }

    @PostMapping(path = "/products/add")
    @PreAuthorize("hasAuthority('product:write')")
    protected String addProductPost(@RequestParam String name,
                                    @RequestParam String description,
                                    @RequestParam Double price) {

        Product product = Product.builder()
                .name(name)
                .price(price)
                .description(description)
                .build();
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping(path = "/products/update")
    @PreAuthorize("hasAuthority('product:write')")
    protected String updateProductGet(@RequestParam Long id,
                                      ModelMap model) {

        Optional<Product> productOptional = productService.getById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);
            return "updateProduct";
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    @PostMapping(path = "/products/update")
    @PreAuthorize("hasAuthority('product:write')")
    protected String updateProductPost(@RequestParam Integer id,
                                       @RequestParam String name,
                                       @RequestParam String description,
                                       @RequestParam Double price) {

        Product product = Product.builder()
                .id(id)
                .price(price)
                .description(description)
                .name(name)
                .build();
        productService.update(product);
        return "redirect:/products";
    }

    @PostMapping(path = "/products/search")
    @PreAuthorize("hasAuthority('product:read')")
    protected String searchProductPost(@RequestParam String search,
                                       ModelMap model) {

        List<Product> bySearch = productService.getBySearch(search);

        model.addAttribute("count", 0);
        model.addAttribute("products", bySearch);
        return "products";
    }

    @PostMapping(path = "/products/delete")
    @PreAuthorize("hasAuthority('product:write')")
    protected String deleteProductPost(@RequestParam Integer id) {

        productService.remove(id);
        return "redirect:/products";
    }
}
