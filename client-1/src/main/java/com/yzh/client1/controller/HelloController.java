package com.yzh.client1.controller;

import com.yzh.bean.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuanzhihao
 * @since 2021/11/26
 */
@RestController
@Slf4j
public class HelloController {
    @Value("${name}")
    private String name;

    @Value("${first.name}")
    private String firstName;

    @GetMapping("/hello")
    public String hello(HttpServletRequest request) {
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
        return "Client1 Hello " + userInfo.getUsername();
    }

    @GetMapping("/sessionId")
    public String sessionId(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        return "Current SessionId is " + sessionId;
    }

    @GetMapping("/headerName")
    public String headerName(HttpServletRequest request) {
        String name = request.getHeader("name");
        return "Header name is " + name;
    }
}
