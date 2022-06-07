package com.yzh.client1.config;

import feign.Client;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;

/**
 * 自定义feignClient
 *
 * @author yuanzhihao
 * @since 2022/1/28
 */
@Configuration
public class FeignConfiguration {
    @Autowired
    private SSLContext sslContext;

    // 注入一个feign.Client对象 加载信任库证书信息 覆盖openFeign默认的client
    @Bean
    public Client feignClient(LoadBalancerClient loadBalancerClient,
                              LoadBalancerClientFactory loadBalancerClientFactory) {
        Client.Default client = new Client.Default(sslContext.getSocketFactory(), NoopHostnameVerifier.INSTANCE);
        return new FeignBlockingLoadBalancerClient(client, loadBalancerClient, loadBalancerClientFactory);
    }
}
