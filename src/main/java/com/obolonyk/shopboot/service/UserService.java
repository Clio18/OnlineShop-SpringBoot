package com.obolonyk.shopboot.service;

import com.obolonyk.shopboot.entity.User;
import com.obolonyk.shopboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User getByLogin(String login) {
        return repository.findByLogin(login).orElseThrow(()-> new RuntimeException(String.format("User with login %s not found", login)));
    }

    public void save(User user) {
        repository.save(user);
    }

}
