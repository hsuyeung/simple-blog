package com.hsuyeung.blog.exception;

/**
 * 资源未找到的异常
 *
 * @author hsuyeung
 * @date 2022/06/20
 */
public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = -6135782581028401974L;

    public NotFoundException(String message) {
        this(message, null);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
