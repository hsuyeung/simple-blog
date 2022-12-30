package com.hsuyeung.blog.constant.enums;

/**
 * 请求速率限制策略枚举
 *
 * @author hsuyeung
 * @date 2022/06/17
 */
public enum ApiRateLimitStrategyEnum {
    /**
     * 根据请求的 ip 地址进行限制
     */
    IP,
    /**
     * 根据用户账户进行限制
     */
    USER
}
