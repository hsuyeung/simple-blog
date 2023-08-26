package com.hsuyeung.blog.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户登录请求参数
 *
 * @author hsuyeung
 * @date 2022/06/29
 */
@ApiModel(description = "用户登录请求参数")
@Data
public class UserLoginDTO implements Serializable {
    private static final long serialVersionUID = -1504299503986897950L;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @Size(max = 32, message = "用户名不能超过 32 个字符")
    private String username;

    @ApiModelProperty(value = "用户密码", required = true)
    @NotBlank(message = "用户密码不能为空")
    @Size(max = 16, message = "密码不能超过 16 个字符")
    private String password;
}
