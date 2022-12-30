package com.hsuyeung.blog.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hsuyeung.blog.constant.enums.LogicDeleteEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据库表公共字段实体类
 *
 * @author hsuyeung
 * @date 2022/06/05
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = -2098692285088879059L;

    /**
     * 主键 id，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private LogicDeleteEnum deleted;
}
