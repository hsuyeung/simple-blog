package com.hsuyeung.blog.web.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * API 接口返回数据统一格式
 *
 * @author hsuyeung
 * @date 2022/02/22
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@Builder
public class WebResponse<T> {
    @ApiModelProperty("状态码")
    private Integer code;

    @ApiModelProperty("提示信息")
    private String msg;

    @ApiModelProperty("返回数据")
    private T data;
}
