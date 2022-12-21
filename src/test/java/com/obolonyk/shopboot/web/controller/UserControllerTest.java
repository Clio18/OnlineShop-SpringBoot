package com.obolonyk.shopboot.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obolonyk.shopboot.entity.User;
import com.obolonyk.shopboot.security.model.UserRole;
import com.obolonyk.shopboot.service.UserService;
import com.obolonyk.shopboot.web.SpringSecurityTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @WithUserDetails("admin")
    @DisplayName("Test Register And Check Status Code And Service Method Calling")
    void testRegister_AndCheckStatus_ServiceMethodCalling() throws Exception {
        User user = User.builder()
                .name("user")
                .login("user")
                .role(UserRole.USER)
                .password("user")
                .email("user@gamil.com")
                .id(1)
                .build();

        User savedUser = User.builder()
                .name("user")
                .role(UserRole.USER)
                .login("user")
                .password("xxxx")
                .email("user@gamil.com")
                .id(1)
                .build();

        when(userService.save(user)).thenReturn(savedUser);
        mockMvc.perform( MockMvcRequestBuilders
                .post("/api/v1/users/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        verify(userService).save(any(User.class));
    }

}