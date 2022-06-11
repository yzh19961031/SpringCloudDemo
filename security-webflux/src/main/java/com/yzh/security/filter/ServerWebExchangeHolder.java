package com.yzh.security.filter;

import org.springframework.web.server.ServerWebExchange;

/**
 * 存储当前线程的ServerWebExchange对象 不过在webflux应用里面应该不适用？？
 *
 * @author yuanzhihao
 * @since 2022/6/11
 */
public class ServerWebExchangeHolder {
    private static final ThreadLocal<ServerWebExchange> CONTEXT = new InheritableThreadLocal<>();

    private ServerWebExchangeHolder() {}

    public static void setContext(ServerWebExchange serverWebExchange) {
        CONTEXT.set(serverWebExchange);
    }

    public static ServerWebExchange getCurrent() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
