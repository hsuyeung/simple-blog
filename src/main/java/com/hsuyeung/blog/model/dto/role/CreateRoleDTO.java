package com.hsuyeung.blog.model.dto.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 创建角色请求参数
 *
 * @author hsuyeung
 * @date 2022/06/29
 */
@ApiModel(description = "创建角色请求参数")
@Data
public class CreateRoleDTO implements Serializable {
    private static final long serialVersionUID = -6899435706413071318L;

    @ApiModelProperty(value = "角色编码", required = true)
    @NotBlank(message = "角色编码不能为空")
    @Size(max = 64, message = "角色编码不能超过 64 个字符")
    private String roleCode;

    @ApiModelProperty(value = "角色描述")
    @Size(max = 255, message = "角色描述不能超过 255 个字符")
    private String roleDesc;

    @ApiModelProperty(value = "是否可用", required = true)
    @NotNull(message = "角色是否可用不能为 null")
    private Boolean enabled;
}
