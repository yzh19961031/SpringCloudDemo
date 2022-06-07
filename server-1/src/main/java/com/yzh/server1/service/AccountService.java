package com.yzh.server1.service;

import com.yzh.server1.bean.Account;

import java.util.List;

/**
 * @author yuanzhihao
 * @since 2022/1/27
 */
public interface AccountService {
    List<Account> queryList();

    int insertAccount(Account account);
}
