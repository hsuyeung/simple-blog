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
 * <p>
 * 文章内容表
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
@TableName("t_content")
public class ContentEntity extends BaseEntity {
    private static final long serialVersionUID = -8940387629564988992L;

    /**
     * markdown 格式文章内容
     */
    @TableField("md_content")
    private String mdContent;

    /**
     * html 格式文章内容
     */
    @TableField("html_content")
    private String htmlContent;
}
