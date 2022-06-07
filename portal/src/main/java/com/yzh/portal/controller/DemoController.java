package com.yzh.portal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuanzhihao
 * @since 2022/5/8
 */
@RequestMapping("/demo")
@RestController
public class DemoController {

    @GetMapping("1")
    public String demo1() {
        return "DEMO1";
    }
}
