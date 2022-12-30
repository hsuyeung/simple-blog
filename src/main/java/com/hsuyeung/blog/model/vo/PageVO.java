package com.hsuyeung.blog.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页数据
 *
 * @author hsuyeung
 * @date 2022/05/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 3013788305392199918L;

    @ApiModelProperty("总数")
    private Long total;

    @ApiModelProperty("数据列表")
    private List<T> data;
}
