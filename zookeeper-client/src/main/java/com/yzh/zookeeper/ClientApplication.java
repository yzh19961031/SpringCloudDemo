package com.yzh.zookeeper;

import com.yzh.zookeeper.feign.ZkServerFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 客户端
 *
 * @author yuanzhihao
 * @since 2022/7/13
 */
@RequestMapping("/zk-client")
@RestController
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients("com.yzh.zookeeper.feign")
@Slf4j
public class ClientApplication {
    RestTemplate restTemplate;

    @Autowired
    private ZkServerFeign serverFeign;

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        this.restTemplate = new RestTemplateBuilder().build();
        return this.restTemplate;
    }

    @GetMapping("/hello/rst")
    public String helloByRestTemplate() {
        String result = restTemplate.getForObject("http://zookeeper-server/zk-server/hello", String.class);
        return "rst " + result;
    }

    @GetMapping("/hello/feign")
    public String helloByFeign() {
        String result = serverFeign.hello();
        return "feign " + result;
    }


    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}
