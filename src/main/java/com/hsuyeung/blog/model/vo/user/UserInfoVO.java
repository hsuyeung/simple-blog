package com.hsuyeung.blog.model.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hsuyeung
 * @date 2022/07/01
 */
@ApiModel(description = "分页列表用户信息")
@Data
public class UserInfoVO implements Serializable {
    private static final long serialVersionUID = -7564794978001708513L;

    @ApiModelProperty("用户 id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("账户是否可用")
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
