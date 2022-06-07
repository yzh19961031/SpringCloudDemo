package com.yzh.gateway;

import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.DiscoveryClient;
import com.yzh.gateway.filter.CustomGatewayFilterFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.SSLContext;

/**
 * @author yuanzhihao
 * @since 2022/4/22
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    @Bean
    public AbstractDiscoveryClientOptionalArgs<?> discoveryClientOptionalArgs(SSLContext sslContext) {
        DiscoveryClient.DiscoveryClientOptionalArgs discoveryClientOptionalArgs = new DiscoveryClient.DiscoveryClientOptionalArgs();
        discoveryClientOptionalArgs.setSSLContext(sslContext);
        return discoveryClientOptionalArgs;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, CustomGatewayFilterFactory filterFactory) {
        return builder
                .routes()
                .route("163", p -> p.path("/163/**")
                .filters(f -> f
                        .filter(filterFactory
                        .apply(new CustomGatewayFilterFactory.Config("Base 163 message", true, false)))
                .redirect(302, "https://www.163.com"))
                .uri("http://localhost"))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
