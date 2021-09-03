package com.me.orderservicedemo.service.feign;

import com.me.orderservicedemo.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderFeignClientCallBack implements OrderFeignClient {
    @Override
    public User findById(Integer id) {
        return new User(1, "LI", "12345", "0.0.0.0", "熔断降级");
    }

    @Override
    public ResponseEntity<Object> findByIdForResp(Integer id) {
        return ResponseEntity.ok(new User(1, "LI", "12345", "0.0.0.0", "Feign触发熔断降级"));
    }
}
