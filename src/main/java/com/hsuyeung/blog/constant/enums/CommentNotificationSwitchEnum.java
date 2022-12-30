package com.hsuyeung.blog.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 评论回复提醒开关枚举
 *
 * @author hsuyeung
 * @date 2022/06/17
 */
@Getter
@AllArgsConstructor
public enum CommentNotificationSwitchEnum {
    /**
     * 不提醒
     */
    OFF(0, "不提醒"),

    /**
     * 提醒
     */
    ON(1, "提醒");

    @EnumValue
    private final Integer code;
    private final String desc;

    public static CommentNotificationSwitchEnum getByCode(Integer code) {
        for (CommentNotificationSwitchEnum notificationSwitchEnum : CommentNotificationSwitchEnum.values()) {
            if (Objects.equals(notificationSwitchEnum.getCode(), code)) {
                return notificationSwitchEnum;
            }
        }
        return null;
    }
}
