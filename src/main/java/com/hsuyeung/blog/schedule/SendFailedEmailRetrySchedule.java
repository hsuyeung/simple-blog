package com.hsuyeung.blog.schedule;

import com.hsuyeung.blog.model.entity.MailEntity;
import com.hsuyeung.blog.service.IMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 发送失败的邮件重试定时任务
 *
 * @author hsuyeung
 * @date 2022/10/18
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SendFailedEmailRetrySchedule {
    private final IMailService mailService;

    /**
     * 十分钟检查一次是否有发送失败的邮件
     */
    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void task() {
        log.info("失败邮件重发定时任务开始");
        List<MailEntity> sendMailList = mailService.listAllSendFailedAndLtMaxRetryNum();
        if (sendMailList.isEmpty()) {
            log.info("失败邮件重发定时任务结束：没有发送失败或者重试次数小于最大重试次数的邮件");
            return;
        }
        sendMailList.forEach(mailEntity -> {
            mailService.retrySendFailedEmail(mailEntity, true);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.error("线程休眠失败", e);
                Thread.currentThread().interrupt();
            }
        });
        log.info("失败邮件重发定时任务结束：执行完成");
    }
}
