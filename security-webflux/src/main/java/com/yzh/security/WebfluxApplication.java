package com.yzh.security;

import com.netflix.discovery.DiscoveryClient;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;

import javax.net.ssl.SSLContext;

/**
 * @author yuanzhihao
 * @since 2022/6/8
 */
@SpringBootApplication
@EnableDiscoveryClient
public class WebfluxApplication {
    @Value("${server.ssl.trust-store}")
    private String trustStorePath;

    @Value("${server.ssl.trust-store-password}")
    private String trustStorePassword;

    @Bean
    public SSLContext sslContext() throws Exception {
        return SSLContextBuilder.
                create().
                loadTrustMaterial(ResourceUtils.getFile(trustStorePath), trustStorePassword.toCharArray()).
                build();
    }

    @Bean
    public DiscoveryClient.DiscoveryClientOptionalArgs discoveryClientOptionalArgs(SSLContext sslContext) {
        DiscoveryClient.DiscoveryClientOptionalArgs discoveryClientOptionalArgs = new DiscoveryClient.DiscoveryClientOptionalArgs();
        discoveryClientOptionalArgs.setSSLContext(sslContext);
        return discoveryClientOptionalArgs;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebfluxApplication.class, args);
    }
}
