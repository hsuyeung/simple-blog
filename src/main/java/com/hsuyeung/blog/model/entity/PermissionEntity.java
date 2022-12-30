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
 * 接口权限表
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("t_permission")
public class PermissionEntity extends BaseEntity {
    private static final long serialVersionUID = 43097764803444971L;

    /**
     * 接口路径
     */
    @TableField("path")
    private String path;

    /**
     * HTTP 方法类型
     */
    @TableField("method")
    private String method;

    /**
     * 接口描述
     */
    @TableField("permission_desc")
    private String permissionDesc;

    /**
     * 是否可用
     */
    @TableField("is_enabled")
    private LogicSwitchEnum enabled;
}
