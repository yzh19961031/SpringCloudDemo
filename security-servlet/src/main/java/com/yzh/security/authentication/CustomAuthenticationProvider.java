package com.yzh.security.authentication;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalCause;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 自定义认证逻辑
 *
 * @author yuanzhihao
 * @since 2022/6/8
 */
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final String username;
    private final String password;

    // 缓存 记录某个IP地址basic认证失败的次数
    private Cache<Object, Integer> basicAuthCache;

    // 最大重试次数 5次
    private final int maxFailedTimes = 5;

    // 锁定时间 30分钟
    private final Duration lockDuration = Duration.ofMinutes(30);

    public CustomAuthenticationProvider(String username, String password) {
        this.username = username;
        this.password = password;
        // 初始化缓存和定时任务
        this.setUp();
    }

    // 自定义认证中添加防暴力破解机制
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String remoteIp = remoteIp();
        // 校验ip是否被锁定
        checkRemoteIpBlocked(remoteIp);
        // 检查账号密码是否合法
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        checkAccountCorrect(username, password, remoteIp);
        // 鉴权成功处理
        authSuccess(remoteIp);
        return new UsernamePasswordAuthenticationToken(username, password,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_admin")));
    }

    // 支持验证的方式
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private void setUp() {
        // 设置缓存 超时时间设置为30分钟 并且添加remove监听
        this.basicAuthCache = CacheBuilder.newBuilder().expireAfterWrite(lockDuration).removalListener(notification -> {
            // 超时移除黑名单时添加日志打印
            if (notification.getCause() == RemovalCause.EXPIRED && (int) notification.getValue() >= maxFailedTimes) {
                log.warn("Remote IP [{}] removed form auth black list automatically", remoteIp());
            }
        }).build();

        // 添加一个定时任务 每天清理一次所有的缓存数据
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(() -> basicAuthCache.cleanUp(), 1, 1, TimeUnit.DAYS);
    }

    private void checkRemoteIpBlocked(String remoteIp) {
        int failedTimes = Optional.ofNullable(basicAuthCache.getIfPresent(remoteIp)).orElse(0);
        if (failedTimes >= maxFailedTimes) {
            log.error("Current IP [{}] are blocked, please try again later", remoteIp);
            throw new LockedException("Current IP is blocked");
        }
    }

    // 登录成功之后 清空缓存中的数据
    private void authSuccess(String remoteIp) {
        int failedTimes = Optional.ofNullable(basicAuthCache.getIfPresent(remoteIp)).orElse(0);
        if (failedTimes >= 1) {
            log.info("IP [{}] Unlocked after auth success", remoteIp);
        }
        basicAuthCache.invalidate(remoteIp);
    }

    private String authFailed(String remoteIp) {
        String res;
        int failedTimes = Optional.ofNullable(basicAuthCache.getIfPresent(remoteIp)).orElse(0);
        if (++failedTimes >= maxFailedTimes) {
            res = "Auth failed and Current IP has been locked";
            log.error(res);
        } else {
            // 剩余重试次数
            int leftTimes = maxFailedTimes - failedTimes;
            res = "Auth failed and has " + leftTimes + " chance left";
         }
        basicAuthCache.put(remoteIp, failedTimes);
        return res;
    }

    private void checkAccountCorrect(String username, String password, String remoteIp) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            log.error("username or password is null");
            throw new BadCredentialsException("username or password is null");
        }
        if ( ! (StringUtils.equals(username, this.username) && StringUtils.equals(password, this.password))) {
            throw new BadCredentialsException(authFailed(remoteIp));
        }
    }

    private String remoteIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "";
        }
        return attributes.getRequest().getRemoteAddr();
    }
}
