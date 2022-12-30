package com.hsuyeung.blog.model.vo.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页列表角色信息
 *
 * @author hsuyeung
 * @date 2022/07/03
 */
@ApiModel(description = "分页列表角色信息")
@Data
public class RoleInfoVO implements Serializable {
    private static final long serialVersionUID = -1452250785010877478L;

    @ApiModelProperty("角色 id")
    private Long id;

    @ApiModelProperty("角色编码")
    private String roleCode;

    @ApiModelProperty("角色描述")
    private String roleDesc;

    @ApiModelProperty("角色是否可用")
    private Boolean enabled;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("更新时间")
    private String updateTime;

    @ApiModelProperty("更新人")
    private String updateBy;
}
