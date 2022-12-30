package com.hsuyeung.blog.model.dto.systemconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 更新系统配置请求参数
 *
 * @author hsuyeung
 * @date 2022/07/06
 */
@ApiModel(description = "更新系统配置请求参数")
@Data
public class UpdateSystemConfigRequestDTO implements Serializable {
    private static final long serialVersionUID = -7576156634860516726L;

    @ApiModelProperty(value = "配置 id", required = true)
    @NotNull(message = "id 不能为空")
    private Long id;

    @ApiModelProperty(value = "配置 value", required = true)
    @NotBlank(message = "confValue 不能为空")
    private String confValue;

    @ApiModelProperty(value = "配置是否可用", required = true)
    @NotNull(message = "enabled 不能为 null")
    private Boolean enabled;
}
