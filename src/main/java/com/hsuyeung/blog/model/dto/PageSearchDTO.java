package com.hsuyeung.blog.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 分页搜索参数
 *
 * @author hsuyeung
 * @date 2022/11/04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页搜索参数")
public class PageSearchDTO<T> {

    @ApiModelProperty("查询参数实体")
    @Valid
    private T searchParam;

    @ApiModelProperty("分页参数实体")
    @NotNull(message = "pageParam 不能为 null")
    @Valid
    private PageDTO pageParam;
}
