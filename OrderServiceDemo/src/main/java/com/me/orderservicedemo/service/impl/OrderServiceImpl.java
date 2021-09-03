package com.me.orderservicedemo.service.impl;

import com.me.orderservicedemo.model.entity.Order;
import com.me.orderservicedemo.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public List<Order> fidAll(String name) {
        return Collections.emptyList();
    }
}
