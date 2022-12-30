package com.hsuyeung.blog.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 逻辑删除枚举
 *
 * @author hsuyeung
 * @date 2022/02/21
 */
@Getter
@AllArgsConstructor
public enum LogicDeleteEnum {

    /**
     * 未删除
     */
    NOT_DELETED(0, "未删除"),

    /**
     * 已删除
     */
    DELETED(1, "已删除");

    @EnumValue
    private final Integer code;
    private final String desc;

    /**
     * 根据 code 获取枚举
     *
     * @param code code
     * @return 如果存在该 code 的枚举值则返回该对象，否则返回 null
     */
    public static LogicDeleteEnum getByCode(Integer code) {
        for (LogicDeleteEnum logicDeleteEnum : LogicDeleteEnum.values()) {
            if (Objects.equals(code, logicDeleteEnum.getCode())) {
                return logicDeleteEnum;
            }
        }
        return null;
    }

}
