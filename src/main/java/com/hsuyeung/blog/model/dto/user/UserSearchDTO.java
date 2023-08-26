package com.hsuyeung.blog.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户分页搜索条件
 *
 * @author hsuyeung
 * @since 2023/8/26
 */
@Data
@ApiModel(description = "用户分页搜索条件")
public class UserSearchDTO implements Serializable {
    private static final long serialVersionUID = -6812909647978100695L;

    @ApiParam("用户名")
    @Size(max = 32, message = "用户名不能超过 32 个字符")
    private String username;

    @ApiParam("昵称")
    @Size(max = 64, message = "昵称不能超过 64 个字符")
    private String nickname;

    @ApiParam("是否可用")
    private Boolean enabled;
}
