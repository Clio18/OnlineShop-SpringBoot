package com.obolonyk.shopboot.web;

import com.obolonyk.shopboot.entity.User;
import com.obolonyk.shopboot.security.model.UserRole;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@TestConfiguration
public class SpringSecurityTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        User admin = User.builder()
                .name("admin")
                .lastName("admin")
                .email("admin@gmail.com")
                .login("admin")
                .password("password")
                .role(UserRole.ADMIN)
                .grantedAuthorities(UserRole.ADMIN.getGrantedAuthorities())
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();
        User user = User.builder()
                .name("user")
                .lastName("user")
                .email("user@gmail.com")
                .login("user")
                .password("password")
                .role(UserRole.USER)
                .grantedAuthorities(UserRole.USER.getGrantedAuthorities())
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();
        return new InMemoryUserDetailsManager(Arrays.asList(
                admin, user
        ));
    }
}
