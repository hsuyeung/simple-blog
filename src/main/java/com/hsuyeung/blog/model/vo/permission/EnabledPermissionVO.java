package com.hsuyeung.blog.model.vo.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 可用状态的权限信息
 *
 * @author hsuyeung
 * @date 2022/07/03
 */
@ApiModel(description = "可用状态的权限信息")
@Data
public class EnabledPermissionVO implements Serializable {
    private static final long serialVersionUID = 1295131438818399160L;

    @ApiModelProperty("权限 id")
    private Long id;

    @ApiModelProperty("权限描述")
    private String permissionDesc;
}
