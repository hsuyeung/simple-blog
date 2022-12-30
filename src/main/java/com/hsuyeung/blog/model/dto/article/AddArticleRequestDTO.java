package com.hsuyeung.blog.model.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新增文章请求参数
 *
 * @author hsuyeung
 * @date 2022/07/08
 */
@ApiModel(description = "新增文章请求参数")
@Data
public class AddArticleRequestDTO implements Serializable {
    private static final long serialVersionUID = -6272807305688452217L;

    @ApiModelProperty(value = "文章标题", required = true)
    @NotBlank(message = "文章标题不能为空")
    private String title;

    @ApiModelProperty(value = "文章路由", required = true)
    @NotBlank(message = "文章路由不能为空")
    private String route;

    @ApiModelProperty(value = "文章作者", required = true)
    @NotBlank(message = "文章作者不能为空")
    private String author;

    @ApiModelProperty(value = "文章关键词", required = true)
    @NotBlank(message = "文章关键词不能为空")
    private String keywords;

    @ApiModelProperty(value = "文章描述", required = true)
    @NotBlank(message = "文章描述不能为空")
    private String description;

    @ApiModelProperty(value = "文章是否置顶", required = true)
    @NotNull(message = "文章是否置顶不能为空")
    private Boolean pin;

    @ApiModelProperty(value = "文章 markdown 格式的内容", required = true)
    @NotBlank(message = "文章 markdown 格式内容不能为空")
    private String mdContent;

    @ApiModelProperty(value = "文章 HTML 格式的内容", required = true)
    @NotBlank(message = "文章 HTML 格式内容不能为空")
    private String htmlContent;
}
