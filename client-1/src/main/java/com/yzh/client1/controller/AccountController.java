package com.yzh.client1.controller;

import com.yzh.client1.bean.Account;
import com.yzh.client1.feign.AccountFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuanzhihao
 * @since 2022/1/28
 */
@RequestMapping("/account")
@RestController
@Slf4j
public class AccountController {
    // 注入feignClient
    @Autowired
    private AccountFeignClient accountFeignClient;

    @GetMapping("/list")
    public List<Account> list() {
        return accountFeignClient.getAccountList();
    }

    @PostMapping("/add")
    public String insertAccount(@RequestBody Account account) {
        return accountFeignClient.addAccount(account);
    }
}
