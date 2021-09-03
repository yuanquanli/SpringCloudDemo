package com.me.service.impl;

import com.me.model.entity.User;
import com.me.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Value("${spring.cloud.client.ip-address}")
    private String ip;
    @Value("${server.port}")
    private String port;

    @Override
    public List<User> findAll(String name) {
        List<User> users = new ArrayList<>();

        String address = ip + ":" + port;
        User user = new User(1, "li", "123456", address, "none");
        users.add(user);
        user = new User(2, "chen", "123456", address, "none");
        users.add(user);

        return users;
    }

    @Override
    public User findById(Integer id) {
        logger.info("接收到的参数:{}", id);
        String address = ip + ":" + port;
        return new User(id, "li", "123456", address, "none");
    }
}
