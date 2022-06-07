package com.yzh.zuul.filter;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 自定义过滤器
 *
 * @author yuanzhihao
 * @since 2022/4/27
 */
@Component
@Slf4j
public class RequestRateLimitFilter implements Filter {

    private static final Cache<String, RateLimiter> RATE_LIMITER_CACHE = CacheBuilder
            .newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();

    private static final double DEFAULT_PERMITS_PER_SECOND = 1; // 令牌桶每秒填充速率


    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String remoteAddr = servletRequest.getRemoteAddr();
        RateLimiter rateLimiter = RATE_LIMITER_CACHE.get(remoteAddr, () -> RateLimiter.create(DEFAULT_PERMITS_PER_SECOND));
        if (rateLimiter.tryAcquire()) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            servletResponse.setContentType("application/json;charset=UTF-8");
            servletResponse.getWriter().write("Too Many Request!!!");
        }
    }
}
