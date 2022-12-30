package com.hsuyeung.blog.model.vo.customconfig;

import lombok.*;

import java.io.Serializable;

/**
 * 页面自定义配置基类
 *
 * @author hsuyeung
 * @date 2022/06/22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommonCustomConfigVO implements Serializable {
    private static final long serialVersionUID = 5411769546269514995L;

    private String headerText;
    private String aboutFooterText;
    private String footerCopyright;
    private String footerAboutText;
    private String beianNum;
    private String avatar;
}
