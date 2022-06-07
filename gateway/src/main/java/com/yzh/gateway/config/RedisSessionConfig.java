package com.yzh.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpCookie;
import org.springframework.session.SaveMode;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 指定saveMode为ALWAYS 功能和flushMode类似
 *
 * @author yuanzhihao
 * @since 2022/5/6
 */
@EnableRedisWebSession(saveMode = SaveMode.ALWAYS)
@Configuration
@Slf4j
public class RedisSessionConfig {
    // 覆盖默认实现
    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        return new CustomWebSessionIdResolver();
    }

    private static class CustomWebSessionIdResolver extends CookieWebSessionIdResolver {
        // 重写resolve方法 对SESSION进行base64解码
        @Override
        public List<String> resolveSessionIds(ServerWebExchange exchange) {
            MultiValueMap<String, HttpCookie> cookieMap = exchange.getRequest().getCookies();
            // 获取SESSION
            List<HttpCookie> cookies = cookieMap.get(getCookieName());
            if (cookies == null) {
                return Collections.emptyList();
            }
            return cookies.stream().map(HttpCookie::getValue).map(this::base64Decode).collect(Collectors.toList());
        }

        private String base64Decode(String base64Value) {
            try {
                byte[] decodedCookieBytes = Base64.getDecoder().decode(base64Value);
                return new String(decodedCookieBytes);
            } catch (Exception ex) {
                log.debug("Unable to Base64 decode value: " + base64Value);
                return null;
            }
        }
    }
}