package com.yzh.server1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.FlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author yuanzhihao
 * @since 2022/5/6
 */
@EnableRedisHttpSession(flushMode = FlushMode.IMMEDIATE)
@Configuration
public class RedisSessionConfig {
}