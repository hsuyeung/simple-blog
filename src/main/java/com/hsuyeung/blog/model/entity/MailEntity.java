package com.hsuyeung.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hsuyeung.blog.constant.enums.MailStatusEnum;
import com.hsuyeung.blog.constant.enums.MailTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 邮件
 *
 * @author hsuyeung
 * @date 2020/10/19 23:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("t_mail")
public class MailEntity extends BaseEntity {
    private static final long serialVersionUID = -4849401866548299127L;

    /**
     * 发件人
     */
    @TableField("m_from")
    private String mFrom;

    /**
     * 收件人，多个邮箱用逗号隔开
     */
    @TableField("m_to")
    private String mTo;

    /**
     * 主题
     */
    @TableField("m_subject")
    private String mSubject;

    /**
     * 内容
     */
    @TableField("m_text")
    private String text;

    /**
     * 抄送。多个邮箱用逗号隔开
     */
    @TableField("m_cc")
    private String cc;

    /**
     * 密送。多个邮箱用逗号隔开
     */
    @TableField("m_bcc")
    private String bcc;

    /**
     * 状态
     */
    @TableField("m_status")
    private MailStatusEnum mStatus;

    /**
     * 错误信息
     */
    @TableField("m_err_msg")
    private String errorMsg;

    /**
     * 邮件类型
     */
    @TableField("m_type")
    private MailTypeEnum type;

    /**
     * 发送时间
     */
    @TableField("send_time")
    private LocalDateTime sendTime;

    /**
     * 失败重试次数
     */
    @TableField("retry_num")
    private Integer retryNum;
}
