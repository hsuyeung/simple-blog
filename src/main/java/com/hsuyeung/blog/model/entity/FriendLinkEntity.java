package com.hsuyeung.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 友链表
 *
 * @author hsuyeung
 * @date 2022/06/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("t_friend_link")
public class FriendLinkEntity extends BaseEntity {
    private static final long serialVersionUID = 2956858940933275569L;

    /**
     * 友链名称
     */
    @TableField("link_name")
    private String linkName;

    /**
     * 友链链接
     */
    @TableField("link_url")
    private String linkUrl;

    /**
     * 友链头像
     */
    @TableField("link_avatar")
    private String linkAvatar;

    /**
     * 一句话描述
     */
    @TableField("link_desc")
    private String linkDesc;

    /**
     * 友链分组
     */
    @TableField("link_group")
    private String linkGroup;
}
