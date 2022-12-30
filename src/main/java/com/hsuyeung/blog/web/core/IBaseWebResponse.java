package com.hsuyeung.blog.web.core;


import java.text.MessageFormat;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;


/**
 * 用于提供一些常用的公共响应方法
 *
 * @author hsuyeung
 * @date 2022/02/22
 */
public interface IBaseWebResponse {
    /**
     * 响应成功，无返回值
     *
     * @param <T> {@link Void}
     * @return {@link WebResponse <T>}
     */
    default <T> WebResponse<T> ok() {
        return WebResponse.<T>builder().code(OK.value()).msg(OK.getReasonPhrase()).build();
    }

    /**
     * 响应成功，带返回数据
     *
     * @param data 返回数据
     * @param <T>  返回数据的单体类型
     * @return {@link WebResponse<T>}
     */
    default <T> WebResponse<T> ok(T data) {
        return WebResponse.<T>builder().code(OK.value()).msg(OK.getReasonPhrase()).data(data).build();
    }

    /**
     * 响应失败，不带返回数据
     *
     * @param <T> {@link Void}
     * @return {@link WebResponse<T>}
     */
    default <T> WebResponse<T> defaultErr() {
        return WebResponse.<T>builder().code(INTERNAL_SERVER_ERROR.value()).build();
    }

    /**
     * 响应失败，带错误信息
     *
     * @param <T> {@link Void}
     * @param msg 错误信息
     * @return {@link WebResponse<T>}
     */
    default <T> WebResponse<T> defaultErr(String msg) {
        return WebResponse.<T>builder().code(INTERNAL_SERVER_ERROR.value()).msg(msg).build();
    }

    /**
     * 响应失败，带自定义格式的错误信息
     *
     * @param msg  错误信息格式
     * @param args 参数
     * @param <T>  {@link Void}
     * @return {@link WebResponse<T>}
     */
    default <T> WebResponse<T> defaultErrArgs(String msg, Object... args) {
        return WebResponse.<T>builder().code(INTERNAL_SERVER_ERROR.value()).msg(MessageFormat.format(msg, args)).build();
    }

    /**
     * 响应失败，带状态码
     *
     * @param code 状态码
     * @param <T>  {@link Void}
     * @return {@link WebResponse<T>}
     */
    default <T> WebResponse<T> err(int code) {
        return WebResponse.<T>builder().code(code).msg(INTERNAL_SERVER_ERROR.getReasonPhrase()).build();
    }

    /**
     * 响应失败，带状态码和错误信息
     *
     * @param code 状态码
     * @param msg  错误信息
     * @param <T>  {@link Void}
     * @return {@link WebResponse<T>}
     */
    default <T> WebResponse<T> err(int code, String msg) {
        return WebResponse.<T>builder().code(code).msg(msg).build();
    }
}
