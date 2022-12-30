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
 * 文件对象
 *
 * @author hsuyeung
 * @date 2022/06/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("t_file")
public class FileEntity extends BaseEntity {
    private static final long serialVersionUID = 9213071291143400976L;

    /**
     * 文件访问全路径
     */
    @TableField("url")
    private String url;
}
