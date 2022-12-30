package com.hsuyeung.blog.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 接口安全配置
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "security.config")
public class SecurityProperties {
    /**
     * 权限放行列表
     */
    private List<String> excludePathPatterns;

    /**
     * 权限拦截列表
     */
    private List<String> pathPatterns;

    /**
     * 用户 token 名字
     */
    private String tokenName = "token";
}
