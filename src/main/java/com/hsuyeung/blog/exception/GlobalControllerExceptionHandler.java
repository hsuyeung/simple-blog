package com.hsuyeung.blog.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局 Controller 异常处理器
 *
 * @author hsuyeung
 * @date 2022/06/05
 */
@Slf4j
@RestControllerAdvice("com.hsuyeung.blog.web.controller")
public class GlobalControllerExceptionHandler {
    /**
     * 处理系统内部异常
     */
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView processNotFoundException(NotFoundException e) {
        log.error(e.getLocalizedMessage(), e);
        return new ModelAndView("error/404");
    }

    /**
     * 其他异常
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView processException(Exception e) {
        log.error(e.getLocalizedMessage(), e);
        return new ModelAndView("error/500");
    }
}
