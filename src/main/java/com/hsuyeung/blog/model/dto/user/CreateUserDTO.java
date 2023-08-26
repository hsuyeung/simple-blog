package com.hsuyeung.blog.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 创建用户请求参数
 *
 * @author hsuyeung
 * @date 2022/06/29
 */
@ApiModel(description = "创建用户请求参数")
@Data
public class CreateUserDTO implements Serializable {
    private static final long serialVersionUID = 7842116972819958320L;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @Size(max = 32, message = "用户名不能超过 32 个字符")
    private String username;

    @ApiModelProperty(value = "昵称", required = true)
    @NotBlank(message = "昵称不能为空")
    @Size(max = 64, message = "昵称不能超过 64 个字符")
    private String nickname;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    @Size(max = 16, message = "密码不能超过 16 个字符")
    private String password;

    @ApiModelProperty(value = "确认密码", required = true)
    @NotBlank(message = "确认密码不能为空")
    @Size(max = 16, message = "密码不能超过 16 个字符")
    private String reconfirmPassword;

    @ApiModelProperty(value = "用户是否可用", required = true)
    @NotNull(message = "用户是否可用不能为 null")
    private Boolean enabled;
}
