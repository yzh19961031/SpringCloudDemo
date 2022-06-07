package com.yzh.gateway.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * @author yuanzhihao
 * @since 2022/4/22
 */
@Slf4j
@Component
public class CustomGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomGatewayFilterFactory.Config> {

    public CustomGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter(new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                if (config.isPreLogger()) {
                    log.info("CustomGatewayFilterFactory pre message is {}", config.getMessage());
                }

                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    if (config.isPostLogger()) {
                        log.info("CustomGatewayFilterFactory post message is {}", config.getMessage());
                    }
                }));
            }
        }, -1);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("message", "preLogger", "postLogger");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Config {
        private String message;
        private boolean preLogger;
        private boolean postLogger;
    }
}
