package com.hsuyeung.blog.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 逻辑开关枚举
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
@Getter
@AllArgsConstructor
public enum LogicSwitchEnum {

    /**
     * 关闭
     */
    OFF(0, "关闭"),

    /**
     * 开启
     */
    ON(1, "开启");

    @EnumValue
    private final Integer code;
    private final String desc;
}
