package com.hsuyeung.blog.model.vo.friendlink;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 友链信息
 *
 * @author hsuyeung
 * @date 2022/06/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendLinkVO implements Serializable {
    private static final long serialVersionUID = -1619323604291445940L;

    @ApiModelProperty("友链总数")
    private Long totalSize;

    @ApiModelProperty("友链分组列表")
    private List<FriendLinkGroupVO> friendLinkGroupList;
}
