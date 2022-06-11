package com.yzh.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzh.security.authentication.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * SpringSecurity 配置类
 *
 * @author yuanzhihao
 * @since 2022/6/8
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    // 设置鉴权失败时响应json格式
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .failureHandler((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    Map<String, Object> map = new HashMap<>();
                    // 返回异常信息
                    map.put("message", exception.getMessage());
                    PrintWriter writer = response.getWriter();
                    writer.write(new ObjectMapper().writeValueAsString(map));
                    writer.flush();
                    writer.close();
                })
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        // 添加自定义认证逻辑
        auth.authenticationProvider(new CustomAuthenticationProvider(username, password));
    }
}
