package com.hsuyeung.blog.model.vo.customconfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 友链页面自定义配置信息
 *
 * @author hsuyeung
 * @date 2022/06/23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendLinkCustomConfigVO implements Serializable {
    private static final long serialVersionUID = -7440234097998280417L;

    private String friendLinkDesc;
    private String friendLinkKeywords;
    private String friendLinkBannerImg;
}
