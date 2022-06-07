package com.yzh.client1.controller;

import com.yzh.client1.bean.Dog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @since 2021/11/5
 */
@RestController
@RequestMapping("/client1")
@Slf4j
public class Client1Controller {
    private final RestTemplate restTemplate;

    public Client1Controller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("oneDog")
    public Dog oneDog() {
        Dog dog = new Dog();
        dog.setAge(2);
        dog.setName("HH");
        return dog;
    }

    @GetMapping("twoDog")
    public Dog twoDog() {
        log.info("hahaa");
        final ResponseEntity<Dog> forEntity = restTemplate.getForEntity("https://eureka-server1/server1/twoDog",
            Dog.class);
        return forEntity.getBody();
    }
}
