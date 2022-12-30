package com.hsuyeung.blog.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 邮件发送状态枚举
 *
 * @author hsuyeung
 * @date 2022/06/18
 */
@Getter
@AllArgsConstructor
public enum MailStatusEnum {
    /**
     * 发送失败
     */
    FAILED(0, "发送失败"),

    /**
     * 发送成功
     */
    SUCCESS(1, "发送成功");

    @EnumValue
    private final Integer code;
    private final String desc;

    public static MailStatusEnum getByCode(Integer code) {
        for (MailStatusEnum statusEnum : MailStatusEnum.values()) {
            if (Objects.equals(statusEnum.getCode(), code)) {
                return statusEnum;
            }
        }
        return null;
    }
}
