package com.yzh.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author yuanzhihao
 * @since 2022/5/8
 */
@RestController
public class DemoController {
    @GetMapping("/test")
    public Mono<String> test(){
        return Mono.just("testThree");
    }
}
