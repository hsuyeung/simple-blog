package com.hsuyeung.blog.model.vo.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页列表评论信息
 *
 * @author hsuyeung
 * @date 2022/07/07
 */
@ApiModel(description = "分页列表评论信息")
@Data
public class CommentInfoVO implements Serializable {
    private static final long serialVersionUID = 2818155637960948938L;

    @ApiModelProperty("评论 id")
    private Long id;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("评论者头像")
    private String avatar;

    @ApiModelProperty("评论者邮箱")
    private String email;

    @ApiModelProperty("评论者网站地址")
    private String website;

    @ApiModelProperty("评论内容预览地址")
    private String contentPreviewUrl;

    @ApiModelProperty("父级评论人的昵称")
    private String parentNickname;

    @ApiModelProperty("回复的评论人的昵称")
    private String replyNickname;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("文章地址")
    private String articleUrl;

    @ApiModelProperty("评论者的 ip 地址")
    private String ip;

    @ApiModelProperty("是否通过邮件接收回复提醒")
    private Boolean notification;

    @ApiModelProperty("评论时间")
    private String createTime;
}
