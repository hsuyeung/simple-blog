package com.hsuyeung.blog.model.vo.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 权限信息
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
@ApiModel(description = "权限信息")
@Data
public class PermissionVO implements Serializable {
    private static final long serialVersionUID = -925678122207529507L;

    @ApiModelProperty("接口路径")
    private String path;

    @ApiModelProperty("HTTP 方法类型")
    private String method;

    @ApiModelProperty("接口描述")
    private String permissionDesc;

    @ApiModelProperty("是否可用")
    private Boolean enabled;
}
