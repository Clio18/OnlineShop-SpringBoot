package com.obolonyk.shopboot.web.controller;

import com.obolonyk.shopboot.entity.Order;
import com.obolonyk.shopboot.entity.Product;
import com.obolonyk.shopboot.service.CartService;
import com.obolonyk.shopboot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    @GetMapping(path = "/products")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    protected String getAllProducts(Model model,
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
    protected String addProductView() {
        return "addProduct";
    }

    @PostMapping(path = "/products/add")
    @PreAuthorize("hasAuthority('product:write')")
    protected String addProduct(@RequestParam String name,
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
    protected String updateProductView(@RequestParam Integer id,
                                      ModelMap model) {

        Product product = productService.getById(id);
        model.addAttribute("product", product);
        return "updateProduct";
    }

    @PostMapping(path = "/products/update")
    @PreAuthorize("hasAuthority('product:write')")
    protected String updateProduct(@RequestParam Integer id,
                                       @RequestParam String name,
                                       @RequestParam String description,
                                       @RequestParam Double price) {

        Product product = Product.builder()
                .id(id)
                .price(price)
                .description(description)
                .name(name)
                .build();
        productService.save(product);
        return "redirect:/products";
    }

    @PostMapping(path = "/products/search")
    @PreAuthorize("hasAuthority('product:read')")
    protected String searchProduct(@RequestParam String search,
                                       ModelMap model) {

        List<Product> bySearch = productService.getBySearch(search);

        model.addAttribute("count", 0);
        model.addAttribute("products", bySearch);
        return "products";
    }

    @PostMapping(path = "/products/delete")
    @PreAuthorize("hasAuthority('product:write')")
    protected String deleteProduct(@RequestParam Integer id) {

        productService.remove(id);
        return "redirect:/products";
    }
}
