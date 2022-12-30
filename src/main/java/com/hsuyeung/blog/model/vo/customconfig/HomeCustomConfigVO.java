package com.hsuyeung.blog.model.vo.customconfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 博客首页自定义配置
 *
 * @author hsuyeung
 * @date 2022/06/22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeCustomConfigVO implements Serializable {
    private static final long serialVersionUID = 5628038802254781213L;

    private String blogHomeTitle;
    private String blogHomeDesc;
    private String blogHomeKeywords;
    private String blogHomeBannerImg;
}
