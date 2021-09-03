package com.me.service;

import com.me.model.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll(String name);
    User findById(Integer id);
}
