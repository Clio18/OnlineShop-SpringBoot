package com.obolonyk.shopboot.web.controller;

import com.obolonyk.shopboot.entity.User;
import com.obolonyk.shopboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repository;

    @GetMapping("login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("logout")
    public String getLogout() {
        return "redirect:/login";
    }

    @GetMapping("registration")
    public String getRegistered() {
        return "registration";
    }

    @PostMapping(path = "/registration")
    protected String register(@ModelAttribute User user) {
        try {
            repository.save(user);
            return "login";
        }catch (Exception e){
            throw new RuntimeException("Exception occurs during registration");
        }
    }

}
