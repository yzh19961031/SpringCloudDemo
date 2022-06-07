package com.yzh.gateway.config;

import io.netty.handler.ssl.SslContextBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.cloud.gateway.config.HttpClientFactory;
import org.springframework.cloud.gateway.config.HttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.SslProvider;

import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Collections;
import java.util.List;

/**
 * 覆盖gateway默认的httpClient配置 支持jks格式的证书
 *
 * @author yuanzhihao
 * @since 2022/5/12
 */
//@Configuration 我本地的生成的证书签名有问题 暂时先注释
@Slf4j
public class GatewayHttpClientConfig {
    @Value("${server.ssl.trust-store}")
    private String trustStore;

    @Value("${server.ssl.trust-store-password}")
    private String trustStorePassword;

    @Value("${server.ssl.trust-store-type}")
    private String trustStoreType;

    @Bean
    @ConditionalOnMissingBean({ HttpClient.class, HttpClientFactory.class })
    public HttpClientFactory gatewayHttpClientFactory(HttpClientProperties properties, ServerProperties serverProperties) {
        TrustManagerFactory trustManagerFactory = getTrustManagerFactory();
        return new CustomHttpClientFactory(properties, serverProperties, Collections.emptyList(), trustManagerFactory);
    }

    // 加载信任库证书
    private TrustManagerFactory getTrustManagerFactory() {
        TrustManagerFactory trustManagerFactory = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(trustStoreType);
            try (FileInputStream inStream = new FileInputStream(ResourceUtils.getFile(trustStore))) {
                keyStore.load(inStream, trustStorePassword.toCharArray());
            }
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
        } catch (Exception e) {
            log.error("Init TrustManagerFactory Failed", e);
        }
        return trustManagerFactory;
    }

    private static class CustomHttpClientFactory extends HttpClientFactory {
        private final TrustManagerFactory trustManagerFactory;

        public CustomHttpClientFactory(HttpClientProperties properties, ServerProperties serverProperties,
                                       List<HttpClientCustomizer> customizers, TrustManagerFactory trustManagerFactory) {
            super(properties, serverProperties, customizers);
            this.trustManagerFactory = trustManagerFactory;
        }

        @SneakyThrows
        protected HttpClient configureSsl(HttpClient httpClient) {
            HttpClientProperties.Ssl ssl = properties.getSsl();
            SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();
            // 设置信任证书
            sslContextBuilder.trustManager(trustManagerFactory);
            SslProvider sslProvider = SslProvider.builder()
                    .sslContext(sslContextBuilder.build())
                    .handshakeTimeout(ssl.getHandshakeTimeout())
                    .closeNotifyFlushTimeout(ssl.getCloseNotifyFlushTimeout())
                    .closeNotifyReadTimeout(ssl.getCloseNotifyReadTimeout())
                    .build();
            httpClient = httpClient.secure(sslProvider);
            return httpClient;
        }
    }
}
