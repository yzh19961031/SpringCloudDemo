package com.yzh.gateway.filter;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 自定义局部限流
 *
 * @author yuanzhihao
 * @since 2022/4/27
 */
@Component
public class CustomRequestRateLimitGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomRequestRateLimitGatewayFilterFactory.Config> {
    public CustomRequestRateLimitGatewayFilterFactory() {
        super(Config.class);
    }

    private static final Cache<String, RateLimiter> RATE_LIMITER_CACHE = CacheBuilder
            .newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();

    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @SneakyThrows
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                String remoteAddr = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
                RateLimiter rateLimiter = RATE_LIMITER_CACHE.get(remoteAddr, () ->
                        RateLimiter.create(Double.parseDouble(config.getPermitsPerSecond())));
                if (rateLimiter.tryAcquire()) {
                    return chain.filter(exchange);
                }
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                DataBuffer dataBuffer = response.bufferFactory().wrap("Too Many Request!!!".getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(dataBuffer));
            }
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("permitsPerSecond");
    }

    @Data
    public static class Config {
        private String permitsPerSecond; // 令牌桶每秒填充速率
    }
}
