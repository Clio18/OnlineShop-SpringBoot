package com.obolonyk.shopboot.service;

import com.obolonyk.shopboot.entity.User;
import com.obolonyk.shopboot.repository.UserRepository;
import com.obolonyk.shopboot.security.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final String PASSWORD = "password";
    private static final String LOGIN = "user";

    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;


    private User user;

    @BeforeEach
    void init() {
        userService = new UserService(repository, passwordEncoder);
        user = User.
                builder()
                .login(LOGIN)
                .password(PASSWORD)
                .build();
    }

    @Test
    @DisplayName("test getByLogin and check if not null and fields values are similar")
    void testGetByLogin_CheckIfNotNull_CheckFieldsValuesSimilar() {
        when(repository.findByLogin(LOGIN)).thenReturn(Optional.of(user));

        User user = userService.getByLogin(LOGIN);
        assertNotNull(user);
        assertEquals(LOGIN, user.getLogin());
        assertEquals(PASSWORD, user.getPassword());
    }

    @Test
    @DisplayName("test getByLogin if login does not exist and check exception throwing and check message")
    void testGetByLogin_IfLoginDoesNotExist_CheckExceptionThrowingAndMessage(){
        when(repository.findByLogin(LOGIN)).thenThrow(new RuntimeException(String.format("User with login %s not found", LOGIN)));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getByLogin(LOGIN);
        });

        assertEquals(String.format("User with login %s not found", LOGIN), exception.getMessage());
    }

    @Test
    @DisplayName("test save and check calling the passwordEncoder and repository methods")
    void testSave_CheckCallingPasswordEncoderAndRepositoryMethods(){
        when(passwordEncoder.encode(PASSWORD)).thenReturn("xxxx");
        when(repository.save(user)).thenReturn(user);
        userService.save(user);
        verify(passwordEncoder).encode(PASSWORD);
        verify(repository).save(user);
    }

    @Test
    @DisplayName("test save and check setRole and that the password is encoded")
    void testSave_CheckSetRoleAndEncodedPassword(){
        when(passwordEncoder.encode(PASSWORD)).thenReturn("xxxx");
        when(repository.save(user)).thenReturn(user);
        User savedUser = userService.save(user);
        assertEquals(UserRole.USER, savedUser.getRole());
        assertNotEquals("password", savedUser.getPassword());
    }

}