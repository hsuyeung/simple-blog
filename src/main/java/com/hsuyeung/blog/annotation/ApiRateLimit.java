package com.hsuyeung.blog.annotation;

import com.hsuyeung.blog.constant.enums.ApiRateLimitStrategyEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

import static com.hsuyeung.blog.constant.enums.ApiRateLimitStrategyEnum.IP;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * 接口请求速率限制
 *
 * @author hsuyeung
 * @date 2022/06/17
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface ApiRateLimit {
    /**
     * 两次请求间隔的时间，0 表示不限制
     */
    long value() default 0;

    /**
     * 间隔时间的单位，默认为秒
     */
    ChronoUnit timeUnit() default ChronoUnit.SECONDS;

    /**
     * 默认根据请求的 IP 地址进行限流
     */
    ApiRateLimitStrategyEnum strategy() default IP;

    /**
     * 请求超过速率限制时的提示信息
     */
    String message() default "";
}
