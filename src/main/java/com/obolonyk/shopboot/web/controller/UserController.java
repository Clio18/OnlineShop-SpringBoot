package com.obolonyk.shopboot.web.controller;
import com.obolonyk.shopboot.entity.User;
import com.obolonyk.shopboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repository;

    @PutMapping("/registration")
    protected ResponseEntity<User> register(@ModelAttribute User user) {
        try {
            repository.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (Exception e){
            throw new RuntimeException("Exception occurs during registration");
        }
    }

}
