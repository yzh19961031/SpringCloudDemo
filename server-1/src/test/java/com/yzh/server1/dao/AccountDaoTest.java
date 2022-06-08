package com.yzh.server1.dao;

import com.yzh.server1.bean.Account;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yuanzhihao
 * @since 2022/1/27
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class AccountDaoTest {
    @Autowired
    private AccountDao accountDao;

    // 切换为JUnit5注解
    @org.junit.jupiter.api.Test
    public void queryList() {
        List<Account> accountList = accountDao.queryList();
        Assert.assertEquals(3, accountList.size());
        System.out.println(accountList);
    }

    @Test
    public void queryById() {
        Account account = accountDao.queryById(1);
        Assert.assertNotNull(account);
    }

    @Test
    public void insert() {
        Account account = new Account();
        account.setName("zhi").setBalance(500);
        accountDao.insert(account);
    }

    @Test
    public void deleteById() {
        accountDao.deleteById(1);
        Account account = accountDao.queryById(1);
        Assert.assertNull(account);
    }

    @Test
    public void update() {
        Account account = accountDao.queryById(1);
        account.setBalance(950);
        accountDao.update(account);
        Account newAccount = accountDao.queryById(1);
        Assert.assertEquals(950, newAccount.getBalance(), 0);
    }
}