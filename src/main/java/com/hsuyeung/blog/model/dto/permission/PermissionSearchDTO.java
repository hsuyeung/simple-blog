package com.hsuyeung.blog.model.dto.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 权限分页搜索条件
 *
 * @author hsuyeung
 * @since 2023/8/26
 */
@Data
@ApiModel(description = "权限分页搜索条件")
public class PermissionSearchDTO implements Serializable {
    private static final long serialVersionUID = -7274276198414622500L;

    @ApiParam("权限路径")
    @Size(max = 255, message = "接口路径不能超过 255 个字符")
    private String path;

    @ApiParam("请求方法类型")
    @Size(max = 32, message = "HTTP 方法类型不能超过 32 个字符")
    private String method;

    @ApiParam("权限描述")
    @Size(max = 255, message = "接口描述不能超过 255 个字符")
    private String permissionDesc;

    @ApiParam("是否可用")
    private Boolean enabled;

}
