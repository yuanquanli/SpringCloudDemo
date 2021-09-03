package com.me.orderservicedemo.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.me.orderservicedemo.model.entity.User;
import com.me.orderservicedemo.service.feign.OrderFeignClient;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@DefaultProperties(defaultFallback = "defaultFallBack")
public class OrderController {
    @Autowired
    private RestTemplate restTemplate;
    @Qualifier("com.me.orderservicedemo.service.feign.OrderFeignClient")
    @Autowired
    private OrderFeignClient orderFeignClient;

//    /**
//     * 使用RestTemplate来调用微服务
//     * @param id
//     * @return
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<Object> findByIdByRest(@PathVariable Integer id){
//        Map<String, Object> params = new HashMap<>();
//        params.put("id", id);
//
//        User user = restTemplate.exchange("http://user-service/api/users/{id}", HttpMethod.GET, null, User.class, params).getBody();
//
//        return ResponseEntity.ok(user);
//    }

//    /**
//     * 使用RestTemplate配合Hystrix来调用微服务
//     * @param id
//     * @return
//     */
//    @HystrixCommand(fallbackMethod = "orderFallBack")
//    @GetMapping("/{id}")
//    public ResponseEntity<Object> findByIdByRest(@PathVariable Integer id){
//        Map<String, Object> params = new HashMap<>();
//        params.put("id", id);
//
//        User user = restTemplate.exchange("http://user-service/api/users/{id}", HttpMethod.GET, null, User.class, params).getBody();
//
//        return ResponseEntity.ok(user);
//    }

    /**
     * 使用RestTemplate来调用微服务
     * @param id
     * @return
     */
    @SentinelResource(value = "findByIdByRest", blockHandler = "orderBlockHandler", fallback = "orderFallBack")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findByIdByRest(@PathVariable Integer id){
        if (id != 1){
            throw new RuntimeException();
        }

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        User user = restTemplate.exchange("http://user-service/api/users/{id}", HttpMethod.GET, null, User.class, params).getBody();

        return ResponseEntity.ok(user);
    }

//    /**
//     * 使用Feign来调用微服务, 有feign组件就不需要配置RestTemplate
//     * 需要配置FeignClient接口类
//     * @param id
//     * @return
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<Object> findByIdByFeign(@PathVariable Integer id){
//        User user = orderFeignClient.findById(id);
//
//        return ResponseEntity.ok(user);
//    }

//    /**
//     * 使用Feign配合Hystrix来调用微服务, 有feign组件就不需要配置RestTemplate
//     * 需要配置FeignClient接口类
//     * @param id
//     * @return
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<Object> findByIdByHystrix(@PathVariable Integer id){
//        User user = orderFeignClient.findById(id);
//
//        return ResponseEntity.ok(user);
//    }

    /**
     * 统一降级方法, 可以统一返回数据
     * @return
     */
    public ResponseEntity<Object> defaultFallBack(){
        User user = new User(null, null, null, null, "defaultFallBack: 统一熔断降级");

        return ResponseEntity.ok(user);
    }

    /**
     * 降级方法, 异常之类触发的降级, 可以统一返回数据
     * @param id 接受参数
     * @return
     */
    public ResponseEntity<Object> orderFallBack(Integer id){
        User user = new User(id, null, null, null, "orderFallBack: 熔断降级");

        return ResponseEntity.ok(user);
    }

    /**
     * 降级方法
     * @param id 接受参数
     * @return
     */
    public ResponseEntity<Object> orderBlockHandler(Integer id){
        User user = new User(id, null, null, null, "orderBlockHandler: 熔断降级");

        return ResponseEntity.ok(user);
    }

}
