package com.hsuyeung.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hsuyeung.blog.constant.enums.LogicSwitchEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 系统配置表
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
@TableName("t_system_config")
public class SystemConfigEntity extends BaseEntity {
    private static final long serialVersionUID = -3367835041861760759L;

    /**
     * 配置 key
     */
    @TableField("conf_key")
    private String confKey;

    /**
     * 配置 value
     */
    @TableField("conf_value")
    private String confValue;

    /**
     * 配置分组
     */
    @TableField("conf_group")
    private String confGroup;

    /**
     * 配置描述
     */
    @TableField("`description`")
    private String description;

    /**
     * 是否有效（0:无效；1:有效）
     */
    @TableField("is_enabled")
    private LogicSwitchEnum enabled;
}
