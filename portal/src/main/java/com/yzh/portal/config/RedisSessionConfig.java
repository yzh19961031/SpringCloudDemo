package com.yzh.portal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.FlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 指定flushMode为IMMEDIATE 表示立即将session写入redis
 *
 * @author yuanzhihao
 * @since 2022/5/8
 */
@EnableRedisHttpSession(flushMode = FlushMode.IMMEDIATE)
@Configuration
public class RedisSessionConfig {
}
