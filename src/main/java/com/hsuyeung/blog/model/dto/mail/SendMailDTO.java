package com.hsuyeung.blog.model.dto.mail;

import com.hsuyeung.blog.constant.enums.MailTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author hsuyeung
 * @date 2022/06/18
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SendMailDTO {
    /**
     * 发件人
     */
    @NotBlank(message = "发件人不能为空")
    private String mFrom;

    /**
     * 收件人，多个邮箱用逗号隔开
     */
    @NotBlank(message = "邮件收件人不能为空")
    private String mTo;

    /**
     * 主题
     */
    @NotBlank(message = "邮件主题不能为空")
    private String mSubject;

    /**
     * 内容
     */
    @NotBlank(message = "邮件内容不能为空")
    private String text;

    /**
     * 抄送。多个邮箱用逗号隔开
     */
    private String cc;

    /**
     * 密送。多个邮箱用逗号隔开
     */
    private String bcc;

    /**
     * 邮件类型
     */
    @NotNull(message = "邮件类型不能为空")
    private MailTypeEnum type;

    /**
     * 邮件的附件
     */
    private MultipartFile[] multipartFiles;

    /**
     * 发送时间
     */
    @NotNull(message = "发送时间不能为空")
    private LocalDateTime sendTime;
}
