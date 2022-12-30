package com.hsuyeung.blog.model.dto.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 创建角色请求参数
 *
 * @author hsuyeung
 * @date 2022/06/29
 */
@ApiModel(description = "创建角色请求参数")
@Data
public class CreateRoleRequestDTO implements Serializable {
    private static final long serialVersionUID = -6899435706413071318L;

    @ApiModelProperty(value = "角色编码", required = true)
    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    @ApiModelProperty(value = "角色描述")
    private String roleDesc;

    @ApiModelProperty(value = "是否可用", required = true)
    @NotNull(message = "角色是否可用不能为空")
    private Boolean enabled;
}
