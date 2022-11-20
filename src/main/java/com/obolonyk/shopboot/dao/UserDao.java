package com.obolonyk.shopboot.dao;

import com.obolonyk.shopboot.entity.User;

public interface UserDao {

    User getByLogin(String login);

    void save(User user);
}
