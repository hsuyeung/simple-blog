package com.hsuyeung.blog.exception;


import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

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
     * 参数校验失败错误
     * <p>使用 @Valid 注解进行参数校验失败时会抛出该异常异常</p>
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public WebResponse<Void> processConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getLocalizedMessage(), e);
        String errMsg = "参数校验失败";
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
        // 取第一个错误信息返回
        if (iterator.hasNext()) {
            errMsg = iterator.next().getMessage();
        }
        return err(HttpStatus.BAD_REQUEST.value(), errMsg);
    }

    /**
     * <p>表单参数类型转换失败</p>
     * 或者
     * <p>用 @Validated 注解在 controller 层对表单方式提交的参数进行参数校验失败时抛出该类型异常</p>
     */
    @ExceptionHandler(BindException.class)
    public WebResponse<Void> processBindException(BindException e) {
        log.error(e.getLocalizedMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        FieldError firstFieldError = bindingResult.getFieldError();
        String errMsg = Objects.isNull(firstFieldError)
                ? "参数校验失败"
                : firstFieldError.getDefaultMessage();
        return err(HttpStatus.BAD_REQUEST.value(), errMsg);
    }

    /**
     * <p>JSON 类型参数校验失败错误</p>
     * 或者
     * <p>用 @Validated/@valid 注解在 controller 层参数校验失败时抛出改种类型异常</p>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public WebResponse<Void> processMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getLocalizedMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        FieldError firstFieldError = bindingResult.getFieldError();
        String errMsg = Objects.isNull(firstFieldError)
                ? "参数校验失败"
                : firstFieldError.getDefaultMessage();
        return err(HttpStatus.BAD_REQUEST.value(), errMsg);
    }

    /**
     * 表单参数类型转换错误的异常
     * <p>比如要求的参数为 Integer，传入的参数没法转换为 Integer 则会抛出该异常</p>
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public WebResponse<Void> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error(e.getLocalizedMessage(), e);
        return err(HttpStatus.BAD_REQUEST.value(), "参数类型不匹配");
    }

    /**
     * controller 层接收前端参数反序列化成实体类失败时抛出该异常
     * <p>比如参数需要一个 JSON 对象，但是传进来一个空字符串或者一个 []（JSON 数组）就会触发该异常</p>
     * <p>或者是对象中一个 Integer 字段，传入的字符串无法转为 Integer 也会触发该异常</p>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public WebResponse<Void> httpMessageNotReadable(HttpMessageNotReadableException e) {
        log.error(e.getLocalizedMessage(), e);
        return err(HttpStatus.BAD_REQUEST.value(), "参数反序列化失败");
    }

    /**
     * 请求方式不支持的异常
     * <p>比如只支持 GET 请求的接口接收到 GET 以外的请求方式就会抛出该异常</p>
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public WebResponse<Void> httpReqMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.error(e.getLocalizedMessage(), e);
        return err(HttpStatus.NOT_IMPLEMENTED.value(), "请求方式不支持");
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
