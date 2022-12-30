package com.hsuyeung.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 定时任务异步线程池配置
 *
 * @author hsuyeung
 * @date 2022/05/26
 */
@EnableScheduling
@Configuration
public class ScheduleThreadPoolConfig {

    @Bean
    public ThreadPoolTaskScheduler asyncTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(4);
        scheduler.setThreadNamePrefix("async-task-scheduler-");
        scheduler.initialize();
        return scheduler;
    }
}
