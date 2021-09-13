package com.me.configuration;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * SpringCloud自带的限流配置
 */
//@Configuration
public class KeyResolverConfig {
    /**
     * 编写基于请求路径的限流规则
     */
//    @Bean
    public KeyResolver pathKeyResolver(){
        return exchange -> Mono.just(exchange.getRequest().getPath().toString());
    }

    /**
     * 编写基于请求参数的限流规则
     */
//    @Bean
    public KeyResolver userKeyResolver(){
        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getQueryParams().getFirst("userId")));
    }
}
