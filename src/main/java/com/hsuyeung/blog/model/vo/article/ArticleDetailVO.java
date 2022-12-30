package com.hsuyeung.blog.model.vo.article;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文章详情
 *
 * @author hsuyeung
 * @date 2022/06/06
 */
@Data
public class ArticleDetailVO implements Serializable {
    private static final long serialVersionUID = -7853559111414225454L;

    @ApiModelProperty("文章 id")
    private Long id;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章作者")
    private String author;

    @ApiModelProperty("文章关键词")
    private String keywords;

    @ApiModelProperty("文章描述")
    private String description;

    @ApiModelProperty("文章内容")
    private String content;

    @ApiModelProperty("创建时间")
    private String createTime;
}
