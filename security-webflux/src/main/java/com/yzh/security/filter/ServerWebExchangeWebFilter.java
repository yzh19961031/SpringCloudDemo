package com.yzh.security.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author yuanzhihao
 * @since 2022/6/11
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class ServerWebExchangeWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerWebExchangeHolder.setContext(exchange);
        return chain.filter(exchange);
    }
}
