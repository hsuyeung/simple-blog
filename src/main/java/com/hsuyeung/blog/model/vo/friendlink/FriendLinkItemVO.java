package com.hsuyeung.blog.model.vo.friendlink;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 单个友链对象实体类
 *
 * @author hsuyeung
 * @date 2022/06/23
 */
@Data
public class FriendLinkItemVO implements Serializable {
    private static final long serialVersionUID = 9002959025776205204L;

    @ApiModelProperty("友链名称")
    private String linkName;

    @ApiModelProperty("友链链接")
    private String linkUrl;

    @ApiModelProperty("友链头像")
    private String linkAvatar;

    @ApiModelProperty("一句话描述")
    private String linkDesc;
}
