package com.obolonyk.shopboot.service;

import com.obolonyk.shopboot.entity.User;
import com.obolonyk.shopboot.repository.UserRepository;
import com.obolonyk.shopboot.security.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User getByLogin(String login) {
        return repository.findByLogin(login).orElseThrow(() -> new RuntimeException(String.format("User with login %s not found", login)));
    }

    public User save(User user) {
        String password = user.getPassword();
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);
        //user by default
        user.setRole(UserRole.USER);
        repository.save(user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return getByLogin(userName);
    }

}
