package com.hsuyeung.blog.model.vo.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 启用状态的角色信息实体类
 *
 * @author hsuyeung
 * @date 2022/07/02
 */
@ApiModel(description = "启用状态的角色信息")
@Data
public class EnabledRoleVO implements Serializable {
    private static final long serialVersionUID = -5510485238794039162L;

    @ApiModelProperty("角色 id")
    private Long id;

    @ApiModelProperty("角色编码")
    private String roleCode;

    @ApiModelProperty("角色描述")
    private String roleDesc;
}
