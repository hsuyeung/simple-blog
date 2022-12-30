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
 * 角色-权限关系
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
@TableName("t_role_permission")
public class RolePermissionEntity extends BaseEntity {
    private static final long serialVersionUID = 3725411417828926291L;

    /**
     * 角色 id
     */
    @TableField("rid")
    private Long rid;

    /**
     * 权限 id
     */
    @TableField("pid")
    private Long pid;
}
