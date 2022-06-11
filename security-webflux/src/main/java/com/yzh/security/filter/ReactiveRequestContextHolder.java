package com.yzh.security.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * @author yuanzhihao
 * @since 2022/6/8
 */
public class ReactiveRequestContextHolder {
    public static final Class<ServerHttpRequest> CONTEXT_KEY = ServerHttpRequest.class;

    public static Mono<ServerHttpRequest> getRequest() {
        return Mono.deferContextual(Mono::just).cast(Context.class).flatMap(context -> Mono.just(context.get(CONTEXT_KEY)));
    }

    public static Context withServerHttpRequest(ServerHttpRequest request) {
        return Context.of(CONTEXT_KEY, request);
    }
}
