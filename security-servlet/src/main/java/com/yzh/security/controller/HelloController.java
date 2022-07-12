package com.yzh.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuanzhihao
 * @since 2022/6/8
 */
@RequestMapping
@RestController
public class HelloController {

    @GetMapping("hello")
    public String hello() {
        return "hello security servlet";
    }
}
