package com.hsuyeung.blog.sitemap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * sitemap 的 url 节点定义
 * <p>
 * <a href="https://wangwl.net/static/pages/sitemap_format.html">XML站点地图的格式规范</a>
 *
 * @author hsuyeung
 * @date 2023/11/10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class URLNode implements Serializable {
    private static final long serialVersionUID = -3920031698141754541L;
    /**
     * 具体链接
     */
    private String loc;

    /**
     * 最后一次更新时间
     */
    private LocalDateTime lastMod;

    /**
     * 抓取频率
     *
     * @see ChangeFreqEnum
     */
    private ChangeFreqEnum changeFreq;

    /**
     * 权重
     * <p>
     * [0.0-1.0] 之间，默认 0.5
     */
    private double priority = 0.5;
}
