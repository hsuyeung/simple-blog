package com.hsuyeung.blog.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

/**
 * 分页参数
 *
 * @author hsuyeung
 * @date 2022/11/04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页参数")
public class PageDTO {
    @ApiModelProperty("页码")
    @Min(value = 1, message = "页码不能小于 0")
    private Integer pageNum;

    @ApiModelProperty("页大小")
    @Min(value = 1, message = "查询数量不能小于 0")
    private Integer pageSize;
}
