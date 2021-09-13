package com.me.configuration;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import org.junit.jupiter.api.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Sentinel限流配置
 */
@Configuration
public class SentinelGateWayConfig {
    private final Logger logger = LoggerFactory.getLogger(SentinelGateWayConfig.class);

    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public SentinelGateWayConfig(List<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolvers;
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    /**
     * 配置限流的异常处理器:SentinelGatewayBlockExceptionHandler
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler(){
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    /**
     * 配置限流过滤器
     * @return
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter sentinelGateWayFilter(){
        return new SentinelGatewayFilter();
    }

    /**
     * 配置初始化的限流参数
     *  用于指定资源的限流规则.
     *      1.资源名称 (路由id)
     *      2.配置统计时间
     *      3.配置限流阈值
     */
    @PostConstruct
    public void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        //该规则意味着order-service这个服务在单位时间内(1s)只能有一次请求
		rules.add(new GatewayFlowRule("order-service")
				.setCount(1)
				.setIntervalSec(1)
		);

//        rules.add(new GatewayFlowRule("product_api")
//                .setCount(1).setIntervalSec(1)
//        );

        GatewayRuleManager.loadRules(rules);
    }

    /**
     * 自定义限流处理器
     */
    @PostConstruct
    public void initBlockHandler(){
        //设置限流触发后返回的数据
        BlockRequestHandler blockRequestHandler = (serverWebExchange, throwable) -> {
            logger.info("触发了限流回调机制, 错误原因: {}", throwable.getMessage());

            Map<String, Object> map = new HashMap<>();
            map.put("code", 2001);
            map.put("message", "限流");

            return ServerResponse.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(map));
        };

        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }
}
