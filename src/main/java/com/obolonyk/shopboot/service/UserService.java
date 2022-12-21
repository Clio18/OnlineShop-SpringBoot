package com.obolonyk.shopboot.service;

import com.obolonyk.shopboot.entity.User;
import com.obolonyk.shopboot.repository.UserRepository;
import com.obolonyk.shopboot.security.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User save(User user) {
        String password = user.getPassword();
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);

        //USER role by default
        user.setRole(UserRole.USER);
        repository.save(user);
        return user;
    }

    public User getByLogin(String login) {
        return repository.findByLogin(login).orElseThrow(() -> new EntityNotFoundException(String.format("User with login %s not found", login)));
    }
}
