package com.hsuyeung.blog.model.dto.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 评论分页搜索条件
 *
 * @author hsuyeung
 * @since 2023/8/26
 */
@Data
@ApiModel(description = "评论分页搜索条件")
public class CommentSearchDTO implements Serializable {
    private static final long serialVersionUID = -3396763437530165895L;

    @ApiParam("评论人昵称")
    @Size(max = 32, message = "评论者昵称不能超过 32 个字符")
    private String nickname;

    @ApiParam("评论人邮箱")
    @Size(max = 64, message = "评论人邮箱不能超过 64 个字符")
    private String email;

    @ApiParam("评论人网址")
    @Size(max = 255, message = "评论人网址不能超过 255 个字符")
    private String website;

    @ApiParam("父级评论人昵称")
    @Size(max = 32, message = "父级评论人昵称不能超过 32 个字符")
    private String parentNickname;

    @ApiParam("回复评论人昵称")
    @Size(max = 32, message = "回复评论人昵称不能超过 32 个字符")
    private String replyNickname;

    @ApiParam("文章 id")
    private Long articleId;

    @ApiParam("ip")
    @Size(max = 128, message = "ip 不能超过 128 个字符")
    private String ip;

    @ApiParam("是否接收邮件提醒")
    private Integer notification;

    @ApiParam("开始时间戳")
    private Long startTimestamp;

    @ApiParam("结束时间戳")
    private Long endTimestamp;

}
