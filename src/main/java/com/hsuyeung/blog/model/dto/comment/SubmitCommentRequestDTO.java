package com.hsuyeung.blog.model.dto.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 提交评论请求参数实体类
 *
 * @author hsuyeung
 * @date 2022/06/16
 */
@ApiModel(description = "提交评论请求参数")
@Data
public class SubmitCommentRequestDTO implements Serializable {
    private static final long serialVersionUID = 2168326647375039892L;

    @ApiModelProperty(value = "文章 id", required = true)
    @NotNull(message = "文章 id 不能为空")
    private Long articleId;

    @ApiModelProperty("是否接收回复提醒邮件")
    private Boolean isNotificationChecked;

    @ApiModelProperty(value = "一级评论 id", required = true)
    @NotNull(message = "父级评论 id 不能为空")
    private Long parentCommentId;

    @ApiModelProperty(value = "直接回复的评论 id", required = true)
    @NotNull(message = "回复的评论 id 不能为空")
    private Long replyCommentId;

    @ApiModelProperty(value = "评论内容", required = true)
    @Size(min = 1, message = "请输入评论内容")
    private String content;

    @ApiModelProperty(value = "昵称", required = true)
    @Size(min = 1, max = 8, message = "昵称长度限制区间为 (0, 8] 个字符")
    private String nickname;

    @ApiModelProperty(value = "邮箱地址", required = true)
    @Size(max = 64, message = "邮箱地址长度不能超过 64 个字符")
    private String email;

    @ApiModelProperty("网址")
    private String website;
}
