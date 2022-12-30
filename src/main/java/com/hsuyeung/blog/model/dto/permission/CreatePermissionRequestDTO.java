package com.hsuyeung.blog.model.dto.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 创建权限请求参数
 *
 * @author hsuyeung
 * @date 2022/06/30
 */
@ApiModel(description = "创建权限请求参数")
@Data
public class CreatePermissionRequestDTO implements Serializable {
    private static final long serialVersionUID = 3971000705974467539L;

    @ApiModelProperty(value = "接口路径", required = true)
    @NotBlank(message = "接口不能为空")
    private String path;

    @ApiModelProperty(value = "HTTP 方法类型", required = true)
    @NotBlank(message = "HTTP 方法类型不能为空")
    private String method;

    @ApiModelProperty(value = "接口描述", required = true)
    @NotBlank(message = "接口描述不能为空")
    private String permissionDesc;

    @ApiModelProperty(value = "是否可用", required = true)
    @NotNull(message = "是否可用不能为空")
    private Boolean enabled;
}
