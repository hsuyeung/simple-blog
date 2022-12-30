package com.hsuyeung.blog.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 邮件类型枚举
 *
 * @author hsuyeung
 * @date 2022/06/18
 */
@Getter
@AllArgsConstructor
public enum MailTypeEnum {
    /**
     * 未知类型
     */
    UNKNOWN(0, "unknown"),
    /**
     * 评论回复提醒
     */
    COMMENT_BE_REPLIED(1, "评论被回复"),
    /**
     * 文章/留言板收到评论
     */
    BE_COMMENTED(2, "文章/留言板收到评论");

    @EnumValue
    private final Integer code;
    private final String desc;

    public static MailTypeEnum getByCode(Integer code) {
        for (MailTypeEnum typeEnum : MailTypeEnum.values()) {
            if (Objects.equals(typeEnum.getCode(), code)) {
                return typeEnum;
            }
        }
        return null;
    }
}
