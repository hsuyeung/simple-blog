package com.hsuyeung.blog.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author hsuyeung
 * @date 2020/11/12 15:36
 */
@Configuration
@EnableAsync
@Slf4j
public class ThreadPoolConfig {

    @Bean
    public Executor asyncServiceExecutor() {
        log.info("start asyncServiceExecutor...");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(4);
        // 配置最大线程数
        executor.setMaxPoolSize(8);
        // 配置队列大小
        executor.setQueueCapacity(10000);
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-thread-");

        // rejection-policy：当 pool 已经达到 max size 的时候，如何处理新任务
        // 抛弃队列中等待最久的任务，然后把当前任务加入队列中尝试再次提交当前任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        // 执行初始化
        executor.initialize();
        return executor;
    }
}
