package com.hsuyeung.blog.model.vo.friendlink;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页列表友链信息
 *
 * @author hsuyeung
 * @date 2022/07/05
 */
@ApiModel(description = "分页列表友链信息")
@Data
public class FriendLinkInfoVO implements Serializable {
    private static final long serialVersionUID = -3928047852266031557L;

    @ApiModelProperty("友链 id")
    private Long id;

    @ApiModelProperty("友链名称")
    private String linkName;

    @ApiModelProperty("友链链接")
    private String linkUrl;

    @ApiModelProperty("友链头像")
    private String linkAvatar;

    @ApiModelProperty("一句话描述")
    private String linkDesc;

    @ApiModelProperty("友链分组")
    private String linkGroup;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("更新时间")
    private String updateTime;

    @ApiModelProperty("更新人")
    private String updateBy;
}
