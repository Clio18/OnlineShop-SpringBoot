package com.obolonyk.shopboot.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class UserController {

    @GetMapping("login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("logout")
    public String getLogout() {
        return "redirect:/login";
    }

    @GetMapping("registration")
    public String getRegistrated() {
        return "registration";
    }

}
