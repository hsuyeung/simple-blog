package com.hsuyeung.blog.exception;

/**
 * api 请求速率被限制时抛出的异常
 *
 * @author hsuyeung
 * @date 2022/06/17
 */
public class ApiRateLimitException extends RuntimeException {
    private static final long serialVersionUID = -5040009887655650906L;

    public ApiRateLimitException(String message) {
        this(message, null);
    }

    public ApiRateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
