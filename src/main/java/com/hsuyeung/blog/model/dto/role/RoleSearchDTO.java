package com.hsuyeung.blog.model.dto.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 角色分页搜条件
 *
 * @author hsuyeung
 * @since 2023/8/26
 */
@Data
@ApiModel(description = "角色分页搜条件")
public class RoleSearchDTO implements Serializable {
    private static final long serialVersionUID = -7386403798748707272L;

    @ApiParam("角色编码")
    @Size(max = 64, message = "角色编码不能超过 64 个字符")
    private String roleCode;

    @ApiParam("是否可用")
    private Boolean enabled;

}
