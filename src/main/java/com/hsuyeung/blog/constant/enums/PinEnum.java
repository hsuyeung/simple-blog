package com.hsuyeung.blog.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否置顶枚举
 *
 * @author hsuyeung
 * @date 2022/07/26
 */
@Getter
@AllArgsConstructor
public enum PinEnum {
    /**
     * 未置顶
     */
    UN_PIN(0, "未置顶"),

    /**
     * 置顶
     */
    PIN(1, "置顶");

    @EnumValue
    private final Integer code;
    private final String desc;
}
