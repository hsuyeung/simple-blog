package com.hsuyeung.blog.model.vo.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页列表权限信息
 *
 * @author hsuyeung
 * @date 2022/07/04
 */
@ApiModel(description = "分页列表权限信息")
@Data
public class PermissionInfoVO implements Serializable {
    private static final long serialVersionUID = -1532842762404883238L;

    @ApiModelProperty("权限 id")
    private Long id;

    @ApiModelProperty("接口路径")
    private String path;

    @ApiModelProperty("HTTP 方法类型")
    private String method;

    @ApiModelProperty("接口描述")
    private String permissionDesc;

    @ApiModelProperty("是否可用")
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
