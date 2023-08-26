package com.hsuyeung.blog.util;

import com.hsuyeung.blog.model.dto.mail.SendMailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.util.Objects;

/**
 * 邮件工具类
 *
 * @author hsuyeung
 * @date 2020/10/19 23:32
 */
@Component
@RequiredArgsConstructor
public class MailUtil {
    private final JavaMailSender mailSender;

    private static final String COMMA = ",";

    /**
     * 发送普通邮件
     */
    public void sendSimpleEmail(SendMailDTO sendMail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sendMail.getMFrom());
        message.setTo(sendMail.getMTo().split(COMMA));
        if (StringUtils.hasLength(sendMail.getCc())) {
            message.setCc(sendMail.getCc().split(COMMA));
        }
        if (StringUtils.hasLength(sendMail.getBcc())) {
            message.setBcc(sendMail.getBcc().split(COMMA));
        }
        message.setSubject(sendMail.getMSubject());
        message.setText(sendMail.getText());
        message.setSentDate(DateUtil.fromJava8LocalDateToDate(sendMail.getSendTime()));
        mailSender.send(message);
    }

    /**
     * 发送支持富文本和附件的邮件
     */
    public void sendMimeMail(SendMailDTO sendMail) throws MessagingException {
        // true 表示支持复杂类型
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
        messageHelper.setFrom(sendMail.getMFrom());
        messageHelper.setTo(sendMail.getMTo().split(COMMA));
        messageHelper.setSubject(sendMail.getMSubject());
        messageHelper.setText(sendMail.getText(), true);
        if (StringUtils.hasLength(sendMail.getCc())) {
            messageHelper.setCc(sendMail.getCc().split(COMMA));
        }
        if (StringUtils.hasLength(sendMail.getBcc())) {
            messageHelper.setCc(sendMail.getBcc().split(COMMA));
        }
        if (sendMail.getMultipartFiles() != null) {
            for (MultipartFile multipartFile : sendMail.getMultipartFiles()) {
                messageHelper.addAttachment(Objects.requireNonNull(multipartFile.getOriginalFilename()), multipartFile);
            }
        }
        messageHelper.setSentDate(DateUtil.fromJava8LocalDateToDate(sendMail.getSendTime()));
        mailSender.send(messageHelper.getMimeMessage());
    }
}
