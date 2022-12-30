package com.hsuyeung.blog.model.vo.friendlink;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 友链分组信息实体类
 *
 * @author hsuyeung
 * @date 2022/06/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendLinkGroupVO implements Serializable {
    private static final long serialVersionUID = 3655445511565082341L;

    @ApiModelProperty("友链分组")
    private String linkGroup;

    @ApiModelProperty("友链列表")
    private List<FriendLinkItemVO> friendLinkItemList;
}
