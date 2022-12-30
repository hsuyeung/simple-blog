package com.hsuyeung.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hsuyeung.blog.constant.enums.CommentNotificationSwitchEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 博客评论表
 * </p>
 *
 * @author hsuyeung
 * @since 2022/06/05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("t_comment")
public class CommentEntity extends BaseEntity {
    private static final long serialVersionUID = -6222784106460993518L;

    /**
     * 评论者用户名
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 评论者头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 评论者邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 评论者网站地址
     */
    @TableField("website")
    private String website;

    /**
     * 评论内容
     */
    @TableField("content")
    private String content;

    /**
     * 评论所属的一级评论 id，为 0 表示当前评论为一级评论
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 当前评论回复的其他评论 id，为 0 或者是等于 parent_id 表示当前评论为一级评论
     */
    @TableField("reply_id")
    private Long replyId;

    /**
     * 评论的文章 id
     */
    @TableField("article_id")
    private Long articleId;

    /**
     * 评论者的 ip 地址
     */
    @TableField("ip")
    private String ip;

    /**
     * 是否通过邮件接收回复提醒
     */
    @TableField("is_notification")
    private CommentNotificationSwitchEnum notification;
}
