package com.yzh.gateway.filter;

import com.yzh.bean.UserInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 登录过滤器
 *
 * @author yuanzhihao
 * @since 2022/5/6
 */
@Component
@Slf4j
public class LoginGlobalFilter implements GlobalFilter, Ordered {
    private static final List<String> white_List = Arrays.asList("/login", "/logout");

    // 登录地址
    private static final String PORTAL_URL = "https://localhost:7885/gateway/portal/login";

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.err.println("login filter starter");
        // 判断是否登录
        AtomicBoolean isLogin = new AtomicBoolean(false);
        exchange.getSession().subscribe(webSession -> {
            UserInfo userInfo = webSession.getAttribute("userInfo");
            System.err.println("userInfo is " + userInfo);
            if (userInfo != null) {
                isLogin.set(true);
            }
        });
        // 这边添加一个延时， 等待获取到session
        Thread.sleep(50);

        // url白名单
        String path = exchange.getRequest().getURI().getPath();
        boolean isWhiteUrl = white_List.stream().anyMatch(path::endsWith);

        // 登录状态或者在url白名单中 放行
        if (isLogin.get() || isWhiteUrl) {
            return chain.filter(exchange);
        }

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set(HttpHeaders.LOCATION, PORTAL_URL);
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
