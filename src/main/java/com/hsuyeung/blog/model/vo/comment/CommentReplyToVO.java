package com.hsuyeung.blog.model.vo.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用来表示二级评论回复的谁
 *
 * @author hsuyeung
 * @date 2022/06/13
 */
@Data
public class CommentReplyToVO implements Serializable {
    private static final long serialVersionUID = -2216990859462394070L;

    @ApiModelProperty("被回复的评论的 id")
    private Long commentId;

    @ApiModelProperty("被回复人的昵称")
    private String nickname;
}
