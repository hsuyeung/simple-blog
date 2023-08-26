package com.hsuyeung.blog.model.dto.systemconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 系统配置分页搜索条件
 *
 * @author hsuyeung
 * @since 2023/8/26
 */
@Data
@ApiModel(description = "系统配置分页搜索条件")
public class SystemConfigSearchDTO implements Serializable {
    private static final long serialVersionUID = -1445347751276312277L;

    @ApiParam("系统配置 key")
    @Size(max = 64, message = "系统配置 key 不能超过 64 个字符")
    private String key;

    @ApiParam("系统配置分组")
    @Size(max = 255, message = "系统配置分组不能超过 255 个字符")
    private String group;

    @ApiParam("系统配置描述")
    @Size(max = 255, message = "系统配置描述不能超过 255 个字符")
    private String desc;

    @ApiParam("是否可用")
    private Boolean enabled;

}
