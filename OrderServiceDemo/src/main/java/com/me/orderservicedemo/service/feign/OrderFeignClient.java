package com.me.orderservicedemo.service.feign;

import com.me.orderservicedemo.model.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 配置feign需要调用的微服务接口
 */
@FeignClient(name = "user-service", fallback = OrderFeignClientCallBack.class)
public interface OrderFeignClient {
    /**
     * 配置需要调用的微服务接口
     */
    @GetMapping("/api/users/{id}")
    User findById(@PathVariable Integer id);

    /**
     * 配置需要调用的微服务接口
     */
    @GetMapping("/api/users/{id}")
    ResponseEntity<Object> findByIdForResp(@PathVariable Integer id);
}
