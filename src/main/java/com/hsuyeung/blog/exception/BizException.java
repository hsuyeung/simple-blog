package com.hsuyeung.blog.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * 业务异常
 * <p>该异常是可以返回给客户端展示的信息</p>
 *
 * @author hsuyeung
 * @date 2022/05/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizException extends RuntimeException {
    private static final long serialVersionUID = -5040009887655650906L;

    private final Integer code;

    public BizException(String message) {
        this(INTERNAL_SERVER_ERROR.value(), message, null);
    }

    public BizException(String message, Throwable cause) {
        this(INTERNAL_SERVER_ERROR.value(), message, cause);
    }

    public BizException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
