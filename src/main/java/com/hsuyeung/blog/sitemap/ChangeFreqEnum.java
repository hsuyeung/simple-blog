package com.hsuyeung.blog.sitemap;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 站点地图抓取频率枚举
 *
 * @author hsuyeung
 * @date 2023/11/10
 */
@Getter
@AllArgsConstructor
public enum ChangeFreqEnum {

    ALWAYS("always"),
    HOURLY("hourly"),
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    YEARLY("yearly"),
    NEVER("never"),
    ;

    private final String value;
}
