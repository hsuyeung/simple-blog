package com.hsuyeung.blog.model.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 文章搜索条件
 *
 * @author hsuyeung
 * @since 2023/8/25 23:46
 */
@Data
@ApiModel(description = "文章搜索条件")
public class ArticleSearchDTO implements Serializable {
    private static final long serialVersionUID = 2880977410290356980L;

    @ApiParam("标题")
    @Size(max = 64, message = "文章标题不能超过 64 个字符")
    private String title;

    @ApiParam("作者")
    @Size(max = 20, message = "文章作者不能超过 20 个字符")
    private String author;

    @ApiParam("关键词")
    @Size(max = 255, message = "文章关键词不能超过 255 个字符")
    private String keywords;

    @ApiParam("描述")
    @Size(max = 255, message = "文章描述不能超过 255 个字符")
    private String desc;

    @ApiParam("是否置顶")
    private Boolean pin;

    @ApiParam("开始时间戳")
    private Long startTimestamp;

    @ApiParam("结束时间戳")
    private Long endTimestamp;
}
