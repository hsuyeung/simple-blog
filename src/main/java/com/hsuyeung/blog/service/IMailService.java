package com.hsuyeung.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.mail.MailSearchDTO;
import com.hsuyeung.blog.model.dto.mail.SendMailDTO;
import com.hsuyeung.blog.model.entity.MailEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.mail.MailInfoVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 邮件服务
 *
 * @author hsuyeung
 * @date 2021/2/20 14:23
 */
@Validated
public interface IMailService extends IService<MailEntity> {

    /**
     * 异步发送普通邮件
     *
     * @param sendMail 发送邮件参数
     */
    @Async("asyncServiceExecutor")
    void sendSimpleEmail(@Valid SendMailDTO sendMail);

    /**
     * 异步发送异步发送支持富文本和附件的邮件
     *
     * @param sendMail 发送邮件参数
     */
    @Async("asyncServiceExecutor")
    void sendMimeMail(@Valid SendMailDTO sendMail);

    /**
     * 分页查询邮件列表
     *
     * @param pageSearchParam 邮件分页搜索条件
     * @return 邮件分页列表
     */
    PageVO<MailInfoVO> getMailPage(@NotNull(message = "pageSearchParam 不能为 null") @Valid
                                   PageSearchDTO<MailSearchDTO> pageSearchParam);

    /**
     * 获取指定邮件的内容
     *
     * @param mailId 邮件 id
     * @return 内容
     */
    String getMailText(Long mailId);

    /**
     * 重新尝试发送失败的邮件
     *
     * @param mailEntity 邮件实体类
     * @param isMime     是否是富文本邮件
     */
    void retrySendFailedEmail(
            @NotNull(message = "mailEntity 不能为 null") MailEntity mailEntity,
            boolean isMime);

    /**
     * 查询所有发送失败且失败重试次数小于最大重试次数的邮件
     *
     * @return {@link SendMailDTO}
     */
    List<MailEntity> listAllSendFailedAndLtMaxRetryNum();
}
