package com.obolonyk.shopboot.web.controller;
import com.obolonyk.shopboot.entity.User;
import com.obolonyk.shopboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    protected ResponseEntity<User> register(@ModelAttribute User user) {
            userService.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
