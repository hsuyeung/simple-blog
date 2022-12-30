package com.hsuyeung.blog.model.vo.article;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 文章归档页面一个结点的数据
 *
 * @author hsuyeung
 * @date 2022/06/07
 */
@Data
public class ArchiveNode implements Serializable {
    private static final long serialVersionUID = -1031801582904665449L;

    @ApiModelProperty("时间")
    private String time;

    @ApiModelProperty("该结点下的文章列表")
    private List<ArchiveArticleVO> articleList;
}
