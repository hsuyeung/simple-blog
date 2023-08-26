package com.hsuyeung.blog.model.dto.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 文件分页搜索条件
 *
 * @author hsuyeung
 * @since 2023/8/26
 */
@Data
@ApiModel(description = "文件分页搜索条件")
public class FileSearchDTO implements Serializable {
    private static final long serialVersionUID = 1514124102384816338L;

    @ApiParam("文件 url")
    @Size(max = 255, message = "文件 url 不能超过 255 个字符")
    private String url;

    @ApiParam("开始时间戳")
    private Long startTimestamp;

    @ApiParam("结束时间戳")
    private Long endTimestamp;

}
