package com.yzh.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务端
 *
 * @author yuanzhihao
 * @since 2022/7/13
 */
@RequestMapping("/zk-server")
@RestController
@EnableDiscoveryClient
@SpringBootApplication
@Slf4j
public class ServerApplication {

    @GetMapping("/hello")
    public String hello() {
        return "hello zk-server";
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
