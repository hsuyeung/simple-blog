package com.hsuyeung.blog.model.vo.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页列表文章数据
 *
 * @author hsuyeung
 * @date 2022/07/08
 */
@ApiModel(description = "分页列表文章数据")
@Data
public class ArticleInfoVO implements Serializable {
    private static final long serialVersionUID = -5021157866977931188L;

    @ApiModelProperty("文章 id")
    private Long id;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章路由")
    private String route;

    @ApiModelProperty("文章作者")
    private String author;

    @ApiModelProperty("文章关键词")
    private String keywords;

    @ApiModelProperty("文章描述")
    private String description;

    @ApiModelProperty("文章 url")
    private String url;

    @ApiModelProperty("是否置顶")
    private Boolean pin;

    @ApiModelProperty("评论数")
    private Long commentNum;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("更新时间")
    private String updateTime;

    @ApiModelProperty("更新人")
    private String updateBy;

}
