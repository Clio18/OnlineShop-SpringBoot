package com.obolonyk.shopboot.web.controller;

import com.obolonyk.shopboot.dto.UserDto;
import com.obolonyk.shopboot.dto.mapper.EntityMapper;
import com.obolonyk.shopboot.entity.User;
import com.obolonyk.shopboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Autowired
    private EntityMapper mapper;

    //URL: POST http://localhost:8080/api/v1/users/registration
    @PostMapping("/registration")
    protected ResponseEntity<User> register(@ModelAttribute UserDto userDTO) {
        User user = mapper.dtoToEntity(userDTO);
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
