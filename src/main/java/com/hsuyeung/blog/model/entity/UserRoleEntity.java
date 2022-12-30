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
 * 用户-角色关系
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
@TableName("t_user_role")
public class UserRoleEntity extends BaseEntity {
    private static final long serialVersionUID = -1603387007204442533L;

    /**
     * 用户 id
     */
    @TableField("uid")
    private Long uid;

    /**
     * 角色 id
     */
    @TableField("rid")
    private Long rid;
}
