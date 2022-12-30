package com.hsuyeung.blog.config.properties;

import lombok.Data;
import org.apache.http.client.config.RequestConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义 httpclient 的 RequestConfig 配置，只配置了常用的几个参数。
 * <p>需要自定义配置其他参数在这里加上之后，再去 RequestConfigConfig 中配置即可</p>
 *
 * @author hsuyeung
 * @date 2022/04/13
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "http.client.request.config")
public class RequestConfigProperties {
    /**
     * Timeout to get a connection from the connection manager for the Http Client
     * The connection manager could be a pool like PoolingHttpClientConnectionManager.
     * When all connections from the pool are used, then the ConnectionRequestTimeout indicates how long your code should
     * wait for a connection to be freed up.
     *
     * @see RequestConfig#getConnectionRequestTimeout()
     */
    private int connectionRequestTimeout = -1;

    /**
     * Connection Timeout to establish a connection with the server.
     *
     * @see RequestConfig#getConnectTimeout()
     */
    private int connectTimeout = -1;

    /**
     * Timeout between two data packets from the server
     *
     * @see RequestConfig#getSocketTimeout()
     */
    private int socketTimeout = -1;
}
