package com.yzh.security.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 使用context保存ServerHttpRequest对象 实现RequestContextHolder功能
 *
 * @author yuanzhihao
 * @since 2022/6/8
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class ReactiveRequestContextFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        return chain.filter(exchange)
                .contextWrite(ctx -> ReactiveRequestContextHolder.withServerHttpRequest(request));
    }
}
