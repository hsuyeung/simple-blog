package com.hsuyeung.blog.model.vo.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文章标题信息
 *
 * @author hsuyeung
 * @date 2022/07/07
 */
@ApiModel(description = "文章标题信息")
@Data
public class ArticleTitleInfoVO implements Serializable {
    private static final long serialVersionUID = 3559305142735970864L;

    @ApiModelProperty("文章 id")
    private Long id;

    @ApiModelProperty("文章标题")
    private String title;
}
