package com.yzh.client1.config;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

/**
 * 自定义spring config server的配置类
 *
 * @author yuanzhihao
 * @since 2021/11/28
 */
@Configuration
public class CustomConfigServiceBootstrapConfiguration {
    // spring cloud config配置文件信息
    @Autowired
    private ConfigClientProperties clientProperties;

    @Value("${server.ssl.trust-store}")
    private String trustStorePath;

    @Value("${server.ssl.trust-store-password}")
    private String trustStorePassword;

    @Bean
    public ConfigServicePropertySourceLocator configServicePropertySourceLocator() throws Exception {
        ConfigServicePropertySourceLocator configServicePropertySourceLocator =  new ConfigServicePropertySourceLocator(clientProperties);
        // 自定义spring cloud config的restTemplate 加载配置中心的信任库信息
        SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(ResourceUtils.getFile(trustStorePath), trustStorePassword.toCharArray()).build();
        CloseableHttpClient build = HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(build);
        RestTemplate customRestTemplate = new RestTemplate(factory);
        configServicePropertySourceLocator.setRestTemplate(customRestTemplate);
        return configServicePropertySourceLocator;
    }
}
