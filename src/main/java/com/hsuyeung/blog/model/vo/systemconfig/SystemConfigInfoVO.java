package com.hsuyeung.blog.model.vo.systemconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页列表系统配置信息
 *
 * @author hsuyeung
 * @date 2022/07/06
 */
@ApiModel(description = "分页列表系统配置信息")
@Data
public class SystemConfigInfoVO implements Serializable {
    private static final long serialVersionUID = 5297155034521019447L;

    @ApiModelProperty("系统配置 id")
    private Long id;

    @ApiModelProperty("配置 key")
    private String confKey;

    @ApiModelProperty("配置 value")
    private String confValue;

    @ApiModelProperty("配置默认 value")
    private String confDefaultValue;

    @ApiModelProperty("配置分组")
    private String confGroup;

    @ApiModelProperty("配置描述")
    private String description;

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
