package com.hsuyeung.blog.model.vo.article;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 博客首页文章列表实体类
 *
 * @author hsuyeung
 * @date 2022/06/05
 */
@Data
public class HomeArticleVO implements Serializable {
    private static final long serialVersionUID = 4060356956458607980L;

    @ApiModelProperty("文章路由")
    private String route;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("是否置顶")
    private Boolean pin;
}
