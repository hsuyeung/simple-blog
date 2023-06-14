package com.hsuyeung.blog.exception;


import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * 全局 API 异常处理器
 *
 * @author hsueung
 * @date 2022/05/14
 */
@Component
@Slf4j
@RestControllerAdvice("com.hsuyeung.blog.web.api")
public class GlobalApiExceptionHandler implements IBaseWebResponse {

    /**
     * 处理系统内部异常
     */
    @ExceptionHandler(SystemInternalException.class)
    public WebResponse<Void> processSystemInternalException(SystemInternalException e) {
        log.error(e.getLocalizedMessage(), e);
        return defaultErr("服务繁忙，请稍后再试");
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BizException.class)
    public WebResponse<Void> processBizException(BizException e) {
        log.error(String.format("code: %s, msg: %s", e.getCode(), e.getLocalizedMessage()), e);
        return err(e.getCode(), e.getLocalizedMessage());
    }

    /**
     * 处理接口限流的异常
     */
    @ExceptionHandler(ApiRateLimitException.class)
    public WebResponse<Void> processApiRateLimitException(ApiRateLimitException e) {
        String errMsg = e.getLocalizedMessage();
        log.error(errMsg);
        return err(HttpStatus.SERVICE_UNAVAILABLE.value(), errMsg);
    }

    /**
     * 处理 jwt token 失效的异常
     */
    @ExceptionHandler(TokenExpiredException.class)
    public WebResponse<Void> processTokenExpiredException(TokenExpiredException e) {
        log.error(e.getLocalizedMessage());
        return err(UNAUTHORIZED.value(), "token 无效或已过期，请重新登录");
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public WebResponse<Void> processOtherException(Exception e) {
        log.error(e.getLocalizedMessage(), e);
        return defaultErr("服务繁忙，请稍后再试");
    }
}
