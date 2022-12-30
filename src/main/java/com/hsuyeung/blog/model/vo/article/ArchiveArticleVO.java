package com.hsuyeung.blog.model.vo.article;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 归档页面文章信息
 *
 * @author hsuyeung
 * @date 2022/06/07
 */
@Data
public class ArchiveArticleVO implements Serializable {
    private static final long serialVersionUID = 7543957697871029618L;

    @ApiModelProperty("文章路由")
    private String route;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("创建时间")
    private String createTime;
}
