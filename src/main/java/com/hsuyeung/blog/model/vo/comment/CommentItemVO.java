package com.hsuyeung.blog.model.vo.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 页面评论信息实体类
 *
 * @author hsuyeung
 * @date 2022/06/13
 */
@Data
public class CommentItemVO implements Serializable, Comparable<CommentItemVO> {
    private static final long serialVersionUID = -2145077569133320449L;

    @ApiModelProperty("评论 id")
    private Long id;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("时间信息")
    private CommentTimeVO time;

    @ApiModelProperty("用户信息")
    private CommentUserVO user;

    @ApiModelProperty("二级评论列表")
    private List<CommentItemVO> childComments;

    @Override
    public int compareTo(CommentItemVO o) {
        return this.getTime().getFormattedTime().compareTo(o.getTime().getFormattedTime());
    }
}
