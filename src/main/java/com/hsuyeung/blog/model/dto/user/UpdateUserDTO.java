package com.hsuyeung.blog.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 更新用户信息请求参数
 *
 * @author hsuyeung
 * @date 2022/06/29
 */
@ApiModel(description = "更新用户信息请求参数")
@Data
public class UpdateUserDTO implements Serializable {
    private static final long serialVersionUID = -6710749659912700292L;

    @ApiModelProperty(value = "用户 id", required = true)
    @NotNull(message = "用户 id 不能为 null")
    private Long id;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @Size(max = 32, message = "用户名不能超过 32 个字符")
    private String username;

    @ApiModelProperty("旧密码")
    @Size(max = 16, message = "密码不能超过 16 个字符")
    private String oldPassword;

    @ApiModelProperty("新密码")
    @Size(max = 16, message = "密码不能超过 16 个字符")
    private String newPassword;

    @ApiModelProperty("再次确认新密码")
    @Size(max = 16, message = "密码不能超过 16 个字符")
    private String reconfirmNewPassword;

    @ApiModelProperty(value = "昵称", required = true)
    @NotBlank(message = "用户昵称不能为空")
    @Size(max = 64, message = "昵称不能超过 64 个字符")
    private String nickname;

    @ApiModelProperty(value = "是否可用", required = true)
    @NotNull(message = "是否可用不能为空")
    private Boolean enabled;
}
