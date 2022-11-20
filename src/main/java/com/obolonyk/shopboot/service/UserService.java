package com.obolonyk.shopboot.service;

import com.obolonyk.shopboot.dao.UserDao;
import com.obolonyk.shopboot.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDao jdbcUserDao;

    public Optional<User> getByLogin(String login) {
        User user = jdbcUserDao.getByLogin(login);
        if(user==null){
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public void save(User user) {
        jdbcUserDao.save(user);
    }
}
