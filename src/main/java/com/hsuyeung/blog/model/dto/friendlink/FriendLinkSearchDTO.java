package com.hsuyeung.blog.model.dto.friendlink;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 友链分页搜索条件
 *
 * @author hsuyeung
 * @since 2023/8/26
 */
@Data
@ApiModel(description = "友链分页搜索条件")
public class FriendLinkSearchDTO implements Serializable {
    private static final long serialVersionUID = 6880119853911353402L;

    @ApiParam("友链名字")
    @Size(max = 255, message = "友链名称不能超过 255 个字符")
    private String linkName;

    @ApiParam("友链链接")
    @Size(max = 255, message = "友链链接不能超过 255 个字符")
    private String linkUrl;

    @ApiParam("友链描述")
    @Size(max = 255, message = "友链描述不能超过 255 个字符")
    private String linkDesc;

    @ApiParam("友链分组")
    @Size(max = 255, message = "友链分组不能超过 255 个字符")
    private String linkGroup;

}
