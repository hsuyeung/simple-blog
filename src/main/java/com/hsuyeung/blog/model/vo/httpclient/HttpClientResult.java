package com.hsuyeung.blog.model.vo.httpclient;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 封装 httpClient 响应结果
 *
 * @author hsuyeung
 * @date 2021/10/24
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class HttpClientResult implements Serializable {
    private static final long serialVersionUID = -5429992126452039696L;

    /**
     * 响应状态
     */
    private HttpStatus status;

    /**
     * 响应数据
     */
    private String content;

    public HttpClientResult() {
    }

    public HttpClientResult(int statusCode) {
        this(statusCode, null);
    }

    public HttpClientResult(HttpStatus status) {
        this(status, null);
    }

    public HttpClientResult(int statusCode, String content) {
        this(HttpStatus.resolve(statusCode), content);
    }

    public HttpClientResult(HttpStatus status, String content) {
        this.status = status;
        this.content = content;
    }
}
