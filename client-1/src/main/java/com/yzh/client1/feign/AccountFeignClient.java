package com.yzh.client1.feign;

import com.yzh.client1.bean.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author yuanzhihao
 * @since 2022/1/28
 */
@FeignClient(value = "https://eureka-server1") // value修改为"https://{applicationName}"格式
public interface AccountFeignClient {
    @RequestMapping(value = "account/list", method = RequestMethod.GET)
    List<Account> getAccountList();

    @RequestMapping(value = "account/add", method = RequestMethod.POST, consumes = "application/json")
    String addAccount(Account account);
}
