package com.yzh.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 熔断处理
 *
 * @author yuanzhihao
 * @since 2022/5/18
 */
@RestController
public class ErrorController {

    @GetMapping("/error")
    public Mono<String> error() {
        return Mono.just("Service Unreachable");
    }
}
