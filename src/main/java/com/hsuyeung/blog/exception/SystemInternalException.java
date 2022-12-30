package com.hsuyeung.blog.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统内部异常
 * <p>这种异常不应该返回给客户端展示，统一返回系统繁忙并记录异常日志</p>
 *
 * @author hsuyeung
 * @date 2022/05/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemInternalException extends RuntimeException {
    private static final long serialVersionUID = -6887627022008901460L;

    public SystemInternalException(String msg) {
        this(msg, null);
    }

    public SystemInternalException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
