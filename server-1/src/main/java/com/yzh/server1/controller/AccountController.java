package com.yzh.server1.controller;

import com.yzh.server1.bean.Account;
import com.yzh.server1.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuanzhihao
 * @since 2022/1/27
 */
@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/list")
    public List<Account> list() {
        return accountService.queryList();
    }

    @PostMapping("/add")
    public String insertAccount(@RequestBody Account account) {
        accountService.insertAccount(account);
        return "Add account success!";
    }
}
