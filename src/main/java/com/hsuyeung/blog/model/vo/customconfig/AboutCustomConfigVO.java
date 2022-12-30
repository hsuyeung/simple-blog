package com.hsuyeung.blog.model.vo.customconfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 关于页面自定义配置
 *
 * @author hsuyeung
 * @date 2022/06/22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AboutCustomConfigVO implements Serializable {
    private static final long serialVersionUID = -7942228497618235935L;

    private String blogAboutDesc;
    private String blogAboutKeywords;
    private String blogAboutBannerImg;
}
