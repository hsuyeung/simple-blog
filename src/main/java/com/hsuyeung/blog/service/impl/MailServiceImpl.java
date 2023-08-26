package com.hsuyeung.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsuyeung.blog.constant.enums.MailStatusEnum;
import com.hsuyeung.blog.constant.enums.MailTypeEnum;
import com.hsuyeung.blog.mapper.MailMapper;
import com.hsuyeung.blog.model.dto.PageDTO;
import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.mail.MailSearchDTO;
import com.hsuyeung.blog.model.dto.mail.SendMailDTO;
import com.hsuyeung.blog.model.entity.BaseEntity;
import com.hsuyeung.blog.model.entity.MailEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.mail.MailInfoVO;
import com.hsuyeung.blog.service.IMailService;
import com.hsuyeung.blog.service.ISystemConfigService;
import com.hsuyeung.blog.util.BizAssertUtil;
import com.hsuyeung.blog.util.ConvertUtil;
import com.hsuyeung.blog.util.DateUtil;
import com.hsuyeung.blog.util.MailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hsuyeung.blog.constant.DateFormatConstants.FORMAT_YEAR_TO_SECOND;
import static com.hsuyeung.blog.constant.StringConstants.SYSTEM;
import static com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.SYSTEM_BLOG_HOME_URL;
import static com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.SYSTEM_MAIL_RETRY_MAX_NUM;
import static com.hsuyeung.blog.constant.enums.MailStatusEnum.FAILED;
import static com.hsuyeung.blog.constant.enums.MailStatusEnum.SUCCESS;

/**
 * @author hsuyeung
 * @date 2021/2/20 14:23
 */
@Service("mailService")
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl extends ServiceImpl<MailMapper, MailEntity> implements IMailService {
    private final MailUtil mailUtil;
    private final ISystemConfigService systemConfigService;

    @Override
    public void sendSimpleEmail(SendMailDTO sendMail) {
        MailEntity mailEntity = ConvertUtil.convert(sendMail, MailEntity.class);
        try {
            mailUtil.sendSimpleEmail(sendMail);
            mailEntity.setMStatus(SUCCESS);
        } catch (Exception e) {
            log.error("发送普通邮件失败", e);
            String errorMsg = e.getLocalizedMessage();
            mailEntity
                    .setMStatus(FAILED)
                    .setErrorMsg(StringUtils.hasText(errorMsg)
                            ? errorMsg.substring(0, Math.min(errorMsg.length(), 255))
                            : errorMsg);
        }
        // 将邮件发送记录持久化
        mailEntity.setCreateBy(SYSTEM);
        mailEntity.setUpdateBy(SYSTEM);
        save(mailEntity);
    }

    @Override
    public void sendMimeMail(SendMailDTO sendMail) {
        MailEntity mailEntity = ConvertUtil.convert(sendMail, MailEntity.class);
        try {
            mailUtil.sendMimeMail(sendMail);
            mailEntity.setMStatus(SUCCESS);
        } catch (Exception e) {
            log.error("发送富文本邮件失败", e);
            String errorMsg = e.getLocalizedMessage();
            mailEntity
                    .setMStatus(FAILED)
                    .setErrorMsg(StringUtils.hasText(errorMsg)
                            ? errorMsg.substring(0, Math.min(errorMsg.length(), 255))
                            : errorMsg);
        }
        // 将邮件发送记录持久化
        mailEntity.setCreateBy(SYSTEM);
        mailEntity.setUpdateBy(SYSTEM);
        save(mailEntity);
    }

    @Override
    public PageVO<MailInfoVO> getMailPage(PageSearchDTO<MailSearchDTO> pageSearchParam) {
        MailSearchDTO searchParam = pageSearchParam.getSearchParam();
        MailStatusEnum statusEnum = null;
        MailTypeEnum typeEnum = null;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        String from = null;
        String to = null;
        String subject = null;
        String cc = null;
        String bcc = null;
        if (Objects.nonNull(searchParam)) {
            statusEnum = MailStatusEnum.getByCode(searchParam.getStatus());
            typeEnum = MailTypeEnum.getByCode(searchParam.getType());
            Long startTimestamp = searchParam.getStartTimestamp();
            Long endTimestamp = searchParam.getEndTimestamp();
            from = searchParam.getFrom();
            to = searchParam.getTo();
            subject = searchParam.getSubject();
            cc = searchParam.getCc();
            bcc = searchParam.getBcc();

            if (Objects.nonNull(startTimestamp)) {
                startTime = DateUtil.fromLongToJava8LocalDate(startTimestamp);
            }
            if (Objects.nonNull(endTimestamp)) {
                endTime = DateUtil.fromLongToJava8LocalDate(endTimestamp);
            }
        }

        PageDTO pageParam = pageSearchParam.getPageParam();
        Page<MailEntity> entityPage = lambdaQuery()
                .select(MailEntity::getId, MailEntity::getMFrom, MailEntity::getMTo, MailEntity::getMSubject,
                        MailEntity::getCc, MailEntity::getBcc, MailEntity::getMStatus, MailEntity::getErrorMsg,
                        MailEntity::getType, MailEntity::getSendTime, MailEntity::getRetryNum)
                .like(StringUtils.hasLength(from), MailEntity::getMFrom, from)
                .like(StringUtils.hasLength(to), MailEntity::getMTo, to)
                .like(StringUtils.hasLength(subject), MailEntity::getMSubject, subject)
                .like(StringUtils.hasLength(cc), MailEntity::getCc, cc)
                .like(StringUtils.hasLength(bcc), MailEntity::getBcc, bcc)
                .eq(Objects.nonNull(statusEnum), MailEntity::getMStatus, statusEnum)
                .eq(Objects.nonNull(typeEnum), MailEntity::getType, typeEnum)
                .ge(Objects.nonNull(startTime), MailEntity::getSendTime, startTime)
                .le(Objects.nonNull(endTime), MailEntity::getSendTime, endTime)
                .orderByDesc(MailEntity::getSendTime)
                .page(new Page<>(pageParam.getPageNum(), pageParam.getPageSize()));
        List<MailEntity> entityList = entityPage.getRecords();
        if (CollectionUtils.isEmpty(entityList)) {
            return new PageVO<>(0L, Collections.emptyList());
        }
        List<MailInfoVO> voList = entityList.stream().map(entity -> {
            MailInfoVO vo = ConvertUtil.convert(entity, MailInfoVO.class);
            vo.setMStatus(entity.getMStatus().getDesc());
            vo.setType(entity.getType().getDesc());
            vo.setSendTime(DateUtil.formatLocalDateTime(entity.getSendTime(), FORMAT_YEAR_TO_SECOND));
            vo.setTextPreviewUrl(String.format("%s/admin/preview/mail/text/%d",
                    systemConfigService.getConfigValue(SYSTEM_BLOG_HOME_URL, String.class),
                    entity.getId()));
            return vo;
        }).collect(Collectors.toList());
        return new PageVO<>(entityPage.getTotal(), voList);
    }

    @Override
    public String getMailText(Long mailId) {
        MailEntity mailEntity = lambdaQuery().select(MailEntity::getText).eq(MailEntity::getId, mailId).one();
        BizAssertUtil.notNull(mailEntity, "邮件不存在");
        return mailEntity.getText();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void retrySendFailedEmail(MailEntity mailEntity, boolean isMime) {
        if (Objects.equals(mailEntity.getMStatus(), SUCCESS)) {
            log.error("邮件状态为发送成功，不需要重新发送");
            return;
        }
        Long emailId = mailEntity.getId();
        Integer retryNum = mailEntity.getRetryNum();

        Integer mailRetryMaxNum = systemConfigService.getConfigValue(SYSTEM_MAIL_RETRY_MAX_NUM, Integer.class);
        if (retryNum >= mailRetryMaxNum) {
            log.error("重发邮件失败：id 为 {} 的邮件重试次数已达 {} 次，最大允许重试次数为：{} 次", emailId, retryNum, mailRetryMaxNum);
        }
        SendMailDTO sendMail = ConvertUtil.convert(mailEntity, SendMailDTO.class);
        LocalDateTime now = LocalDateTime.now();
        sendMail.setSendTime(now);
        retryNum++;
        try {
            if (isMime) {
                mailUtil.sendMimeMail(sendMail);
            } else {
                mailUtil.sendSimpleEmail(sendMail);
            }
            lambdaUpdate()
                    .set(MailEntity::getRetryNum, retryNum)
                    .set(MailEntity::getSendTime, now)
                    .set(MailEntity::getMStatus, SUCCESS)
                    .set(BaseEntity::getUpdateBy, SYSTEM)
                    .eq(MailEntity::getId, emailId)
                    .update(new MailEntity());
            log.info("id 为 {} 的邮件重发成功", emailId);
        } catch (Exception e) {
            String errMsg = e.getMessage();
            try {
                lambdaUpdate()
                        .set(MailEntity::getRetryNum, retryNum)
                        .set(MailEntity::getSendTime, now)
                        .set(MailEntity::getErrorMsg, StringUtils.hasText(errMsg)
                                ? errMsg.substring(0, Math.min(errMsg.length(), 255))
                                : errMsg)
                        .set(BaseEntity::getUpdateBy, SYSTEM)
                        .eq(MailEntity::getId, emailId)
                        .update(new MailEntity());
            } catch (Exception ex) {
                log.error("保存数据库失败：", ex);
            }
            log.error(String.format("重发邮件失败，当前失败次数：%d", retryNum), e);
        }
    }

    @Override
    public List<MailEntity> listAllSendFailedAndLtMaxRetryNum() {
        return lambdaQuery()
                .select(MailEntity::getId, MailEntity::getMFrom, MailEntity::getMTo, MailEntity::getMSubject,
                        MailEntity::getText, MailEntity::getCc, MailEntity::getBcc, MailEntity::getMStatus,
                        MailEntity::getRetryNum)
                .eq(MailEntity::getMStatus, FAILED)
                .lt(MailEntity::getRetryNum, systemConfigService.getConfigValue(SYSTEM_MAIL_RETRY_MAX_NUM, Integer.class))
                .orderByAsc(MailEntity::getId)
                .list();
    }
}
