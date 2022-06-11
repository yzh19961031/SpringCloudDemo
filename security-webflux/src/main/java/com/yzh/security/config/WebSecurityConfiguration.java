package com.yzh.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzh.security.authentication.CustomReactiveAuthenticationManager;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * SpringSecurity 配置类
 *
 * @author yuanzhihao
 * @since 2022/6/8
 */
@EnableWebFluxSecurity
public class WebSecurityConfiguration {
    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .anyExchange()
                .authenticated()
                .and()
                // 设置自定义鉴权处理
                .authenticationManager(new CustomReactiveAuthenticationManager(username, password))
                .formLogin()
                .authenticationFailureHandler(new ServerAuthenticationFailureHandler() {
                    // 设置鉴权失败时响应json格式
                    @SneakyThrows
                    @Override
                    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
                        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
                        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                        Map<String, Object> map = new HashMap<>();
                        // 返回异常信息
                        map.put("message", exception.getMessage());
                        String value = new ObjectMapper().writeValueAsString(map);
                        DataBuffer dataBuffer = response.bufferFactory().wrap(value.getBytes(StandardCharsets.UTF_8));
                        return response.writeWith(Mono.just(dataBuffer));
                    }
                })
                .and()
                .httpBasic();
        return http.build();
    }
}
