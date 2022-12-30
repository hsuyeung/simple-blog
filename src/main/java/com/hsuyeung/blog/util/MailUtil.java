package com.hsuyeung.blog.util;

import com.hsuyeung.blog.model.dto.mail.SendMailDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.Objects;

/**
 * 邮件工具类
 *
 * @author hsuyeung
 * @date 2020/10/19 23:32
 */
@Component
public class MailUtil {
    private static final String COMMA = ",";
    @Resource
    private JavaMailSender mailSender;

    /**
     * 发送普通邮件
     */
    public void sendSimpleEmail(SendMailDTO sendMailDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sendMailDTO.getMFrom());
        message.setTo(sendMailDTO.getMTo().split(COMMA));
        if (StringUtils.hasLength(sendMailDTO.getCc())) {
            message.setCc(sendMailDTO.getCc().split(COMMA));
        }
        if (StringUtils.hasLength(sendMailDTO.getBcc())) {
            message.setBcc(sendMailDTO.getBcc().split(COMMA));
        }
        message.setSubject(sendMailDTO.getMSubject());
        message.setText(sendMailDTO.getText());
        message.setSentDate(DateUtil.fromJava8LocalDateToDate(sendMailDTO.getSendTime()));
        mailSender.send(message);
    }

    /**
     * 发送支持富文本和附件的邮件
     */
    public void sendMimeMail(SendMailDTO sendMailDTO) throws MessagingException {
        // true 表示支持复杂类型
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
        messageHelper.setFrom(sendMailDTO.getMFrom());
        messageHelper.setTo(sendMailDTO.getMTo().split(COMMA));
        messageHelper.setSubject(sendMailDTO.getMSubject());
        messageHelper.setText(sendMailDTO.getText(), true);
        if (StringUtils.hasLength(sendMailDTO.getCc())) {
            messageHelper.setCc(sendMailDTO.getCc().split(COMMA));
        }
        if (StringUtils.hasLength(sendMailDTO.getBcc())) {
            messageHelper.setCc(sendMailDTO.getBcc().split(COMMA));
        }
        if (sendMailDTO.getMultipartFiles() != null) {
            for (MultipartFile multipartFile : sendMailDTO.getMultipartFiles()) {
                messageHelper.addAttachment(Objects.requireNonNull(multipartFile.getOriginalFilename()), multipartFile);
            }
        }
        messageHelper.setSentDate(DateUtil.fromJava8LocalDateToDate(sendMailDTO.getSendTime()));
        mailSender.send(messageHelper.getMimeMessage());
    }
}
