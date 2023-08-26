package com.hsuyeung.blog.model.dto.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 创建权限请求参数
 *
 * @author hsuyeung
 * @date 2022/06/30
 */
@ApiModel(description = "创建权限请求参数")
@Data
public class CreatePermissionDTO implements Serializable {
    private static final long serialVersionUID = 3971000705974467539L;

    @ApiModelProperty(value = "接口路径", required = true)
    @NotBlank(message = "接口路径不能为空")
    @Size(max = 255, message = "接口路径不能超过 255 个字符")
    private String path;

    @ApiModelProperty(value = "HTTP 方法类型", required = true)
    @NotBlank(message = "HTTP 方法类型不能为空")
    @Size(max = 32, message = "HTTP 方法类型不能超过 32 个字符")
    private String method;

    @ApiModelProperty(value = "接口描述", required = true)
    @NotBlank(message = "接口描述不能为空")
    @Size(max = 255, message = "接口描述不能超过 255 个字符")
    private String permissionDesc;

    @ApiModelProperty(value = "是否可用", required = true)
    @NotNull(message = "是否可用不能为 null")
    private Boolean enabled;
}
