package com.hsuyeung.blog.model.vo.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 评论列表
 *
 * @author hsuyeung
 * @date 2022/06/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO implements Serializable {
    private static final long serialVersionUID = 5312924321193733968L;

    @ApiModelProperty("总数")
    private Long totalSize;

    @ApiModelProperty("评论列表")
    private List<CommentItemVO> commentItemList;
}
