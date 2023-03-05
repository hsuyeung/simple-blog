package com.hsuyeung.blog.rss;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * RSS 字段定义
 *
 * @author hsuyeung
 * @date 2023/03/04
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface Specification {
    /**
     * 字段是否必填，默认 false
     */
    boolean required() default false;

    /**
     * 字段描述
     */
    String description() default "";
}
