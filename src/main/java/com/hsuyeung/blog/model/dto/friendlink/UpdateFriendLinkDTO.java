package com.hsuyeung.blog.model.dto.friendlink;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 更新友链请求参数实体类
 *
 * @author hsuyeung
 * @date 2022/06/25
 */
@ApiModel(description = "更新友链请求参数")
@Data
public class UpdateFriendLinkDTO implements Serializable {
    private static final long serialVersionUID = 7903987898815576207L;

    @ApiModelProperty(value = "友链 id", required = true)
    @NotNull(message = "id 不能为空")
    private Long id;

    @ApiModelProperty(value = "友链名称", required = true)
    @NotBlank(message = "友链名称不能为空")
    private String linkName;

    @ApiModelProperty(value = "友链链接", required = true)
    @NotBlank(message = "友链链接不能为空")
    private String linkUrl;

    @ApiModelProperty(value = "友链头像", required = true)
    @NotBlank(message = "友链头像不能为空")
    private String linkAvatar;

    @ApiModelProperty(value = "一句话描述", required = true)
    private String linkDesc;

    @ApiModelProperty(value = "友链分组", required = true)
    @NotBlank(message = "友链分组不能为空")
    private String linkGroup;
}
