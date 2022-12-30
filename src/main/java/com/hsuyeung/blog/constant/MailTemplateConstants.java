package com.hsuyeung.blog.constant;

import com.hsuyeung.blog.exception.SystemInternalException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Objects;

import static org.apache.commons.codec.CharEncoding.UTF_8;

/**
 * 邮件模板常量
 *
 * @author hsuyeung
 * @date 2022/07/06
 */
public final class MailTemplateConstants {
    public static final String MAIL_COMMENT_BE_REPLIED_TEMPLATE;
    public static final String MAIL_BE_COMMENTED_TEMPLATE;

    static {
        // 加载评论被回复邮件模板
        try (
                InputStream is = MailTemplateConstants.class.getClassLoader().getResourceAsStream("templates/mail/comment_be_replied_template.html");
                ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            byte[] b = new byte[2048];
            int readSize;
            while (((readSize = Objects.requireNonNull(is).read(b)) > 0)) {
                baos.write(b, 0, readSize);
            }
            MAIL_COMMENT_BE_REPLIED_TEMPLATE = baos.toString(UTF_8);
        } catch (Exception e) {
            throw new SystemInternalException("加载 comment_be_replied_template 邮件模板失败", e);
        }

        // 加载博客被评论邮件模板
        try (
                InputStream is = MailTemplateConstants.class.getClassLoader().getResourceAsStream("templates/mail/be_commented_template.html");
                ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            byte[] b = new byte[2048];
            int readSize;
            while (((readSize = Objects.requireNonNull(is).read(b)) > 0)) {
                baos.write(b, 0, readSize);
            }
            MAIL_BE_COMMENTED_TEMPLATE = baos.toString(UTF_8);
        } catch (Exception e) {
            throw new SystemInternalException("加载 be_commented_template 邮件模板失败", e);
        }
    }

    private MailTemplateConstants() {
    }
}
