package com.yzh.server1.controller;

import com.yzh.bean.UserInfo;
import com.yzh.server1.bean.Dog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @since 2021/11/5
 */
@RestController
@Slf4j
public class Server1Controller {

    @GetMapping("/hello")
    public String hello(HttpServletRequest request) {
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
        return "Server1 Hello " + userInfo.getUsername();
    }

    @GetMapping("/sessionId")
    public String sessionId(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        return "Current SessionId is " + sessionId;
    }

    @GetMapping("twoDog")
    public Dog oneDog() {
        Dog dog = new Dog();
        dog.setAge(2);
        dog.setName("HH");
        log.info("server1 ");
        return dog;
    }
}
