package com.hsuyeung.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hsuyeung.blog.constant.enums.PinEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 博客文章表
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
@TableName("t_article")
public class ArticleEntity extends BaseEntity {
    private static final long serialVersionUID = 4362214654700368692L;

    /**
     * 文章标题
     */
    @TableField("title")
    private String title;

    /**
     * 文章路由
     */
    @TableField("route")
    private String route;

    /**
     * 文章作者
     */
    @TableField("author")
    private String author;

    /**
     * 文章关键词，多个用逗号分隔
     */
    @TableField("keywords")
    private String keywords;

    /**
     * 文章描述
     */
    @TableField("description")
    private String description;

    /**
     * 内容 id
     */
    @TableField("content_id")
    private Long contentId;

    /**
     * 是否置顶（0: 不置顶；1: 置顶）
     */
    @TableField("is_pin")
    private PinEnum pin;
}
