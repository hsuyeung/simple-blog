package com.hsuyeung.blog.model.dto.friendlink;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull(message = "id 不能为 null")
    private Long id;

    @ApiModelProperty(value = "友链名称", required = true)
    @NotBlank(message = "友链名称不能为空")
    @Size(max = 255, message = "友链名称不能超过 255 个字符")
    private String linkName;

    @ApiModelProperty(value = "友链链接", required = true)
    @NotBlank(message = "友链链接不能为空")
    @Size(max = 255, message = "友链链接不能超过 255 个字符")
    private String linkUrl;

    @ApiModelProperty(value = "友链头像", required = true)
    @NotBlank(message = "友链头像不能为空")
    @Size(max = 255, message = "友链头像不能超过 255 个字符")
    private String linkAvatar;

    @ApiModelProperty(value = "一句话描述", required = true)
    @Size(max = 255, message = "一句话描述不能超过 255 个字符")
    private String linkDesc;

    @ApiModelProperty(value = "友链分组", required = true)
    @NotBlank(message = "友链分组不能为空")
    @Size(max = 255, message = "友链分组不能超过 255 个字符")
    private String linkGroup;
}
