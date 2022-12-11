package com.obolonyk.shopboot.service;

import com.obolonyk.shopboot.entity.User;
import com.obolonyk.shopboot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    private static final String password = "password";
    private static final String login = "user";

    private User user;

    @BeforeEach
    void init() {
        userService = new UserService(repository, passwordEncoder);
        user = User.
                builder()
                .login(login)
                .password(password)
                .build();
    }

    @Test
    void testGetByLogin_CheckIfNotNull_CheckFieldsValuesSimilar() {
        when(repository.findByLogin(login)).thenReturn(Optional.of(user));

        User user = userService.getByLogin(login);
        assertNotNull(user);
        assertEquals(login, user.getLogin());
        assertEquals(password, user.getPassword());
    }

    @Test
    void testGetByLogin_IfLoginDoesNotExist_CheckExceptionThrowingAndMessage(){
        when(repository.findByLogin(login)).thenThrow(new RuntimeException(String.format("User with login %s not found", login)));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getByLogin(login);
        });

        assertEquals(String.format("User with login %s not found", login), exception.getMessage());
    }

    @Test
    void testLoadUserByUsername_CheckIfNotNull_CheckFieldsValuesSimilar() {
        when(repository.findByLogin(login)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(login);
        assertNotNull(userDetails);
        assertEquals(login, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
    }

    @Test
    void testSave_CheckCallingPasswordEncoderAndRepositoryMethods(){
        when(passwordEncoder.encode(password)).thenReturn("xxxx");
        when(repository.save(user)).thenReturn(user);
        userService.save(user);
        verify(passwordEncoder).encode(password);
        verify(repository).save(user);
    }

}