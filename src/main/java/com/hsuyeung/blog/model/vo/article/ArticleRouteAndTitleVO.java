package com.hsuyeung.blog.model.vo.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文章标题和路由信息实体类
 *
 * @author hsuyeung
 * @date 2022/06/21
 */
@ApiModel(description = "文章标题和路由信息实体类")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleRouteAndTitleVO implements Serializable {
    private static final long serialVersionUID = -1623338134333686988L;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章路由")
    private String route;
}
