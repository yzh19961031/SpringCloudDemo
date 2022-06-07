package com.yzh.server1.service.impl;

import com.yzh.server1.bean.Account;
import com.yzh.server1.dao.AccountDao;
import com.yzh.server1.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yuanzhihao
 * @since 2022/1/27
 */
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    @Override
    public List<Account> queryList() {
        return accountDao.queryList();
    }

    @Override
    public int insertAccount(Account account) {
        return accountDao.insert(account);
    }
}
