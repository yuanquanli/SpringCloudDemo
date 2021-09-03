package com.me.orderservicedemo.service;

import com.me.orderservicedemo.model.entity.Order;

import java.util.List;

public interface OrderService {
    List<Order> fidAll(String name);
}
