package com.hsuyeung.blog.model.vo.customconfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 归档页面自定义配置
 *
 * @author hsuyeung
 * @date 2022/06/22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveCustomConfigVO implements Serializable {
    private static final long serialVersionUID = -8302135040002502246L;

    private String blogArchiveDesc;
    private String blogArchiveKeywords;
    private String blogArchiveBannerImg;
}
