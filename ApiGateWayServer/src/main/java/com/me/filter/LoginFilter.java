package com.me.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoginFilter implements GlobalFilter, Ordered {
    private Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("执行了全局过滤器");
        String accessToken = exchange.getRequest().getQueryParams().getFirst("access-token");
        if (accessToken == null){
            logger.info("没有登录");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            //标记请求结束
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
