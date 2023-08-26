package com.hsuyeung.blog.config;

import com.hsuyeung.blog.config.properties.RequestConfigProperties;
import com.hsuyeung.blog.util.HttpClientUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.config.RequestConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 根据 {@link RequestConfigProperties} 自定义的配置定义 RequestConfig Bean，用于注入到 {@link HttpClientUtil} 中使用
 *
 * @author hsuyeung
 * @date 2022/04/13
 */
@Configuration
@RequiredArgsConstructor
public class RequestConfigConfig {
    private final RequestConfigProperties requestConfigProperties;

    /**
     * 自定义的 RequestConfig Bean
     *
     * @return {@link RequestConfig}
     */
    @Bean
    @Primary
    public RequestConfig customRequestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(requestConfigProperties.getConnectionRequestTimeout())
                .setConnectTimeout(requestConfigProperties.getConnectTimeout())
                .setSocketTimeout(requestConfigProperties.getSocketTimeout())
                .build();
    }
}
