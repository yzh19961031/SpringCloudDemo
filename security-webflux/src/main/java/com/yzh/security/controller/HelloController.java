package com.yzh.security.controller;

import com.yzh.security.filter.ReactiveRequestContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author yuanzhihao
 * @since 2022/6/8
 */
@RequestMapping
@RestController
public class HelloController {

    @GetMapping("hello")
    public Mono<String> hello() {
        return Mono.just("hello security servlet");
    }

    /**
     * 获取当前请求IP
     *
     * @return IP
     */
    @GetMapping("remoteIp")
    public Mono<String> remoteIp() {
        return ReactiveRequestContextHolder.getRequest()
                .map(serverHttpRequest ->
                        Objects.requireNonNull(serverHttpRequest.getRemoteAddress()).getAddress().getHostAddress());
    }
}
