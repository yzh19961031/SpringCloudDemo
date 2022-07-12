package com.yzh.zookeeper.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author yuanzhihao
 * @since 2022/7/13
 */
@FeignClient(value = "zookeeper-server")
public interface ZkServerFeign {

    @RequestMapping(value = "zk-server/hello", method = RequestMethod.GET)
    String hello();
}
