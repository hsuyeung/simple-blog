package com.hsuyeung.blog.aop;

import com.hsuyeung.blog.annotation.ApiRateLimit;
import com.hsuyeung.blog.constant.SystemConfigConstants;
import com.hsuyeung.blog.exception.ApiRateLimitException;
import com.hsuyeung.blog.service.ISystemConfigService;
import com.hsuyeung.blog.util.AssertUtil;
import com.hsuyeung.blog.util.DateUtil;
import com.hsuyeung.blog.util.IpUtil;
import com.hsuyeung.blog.util.RedisUtil;
import com.hsuyeung.blog.web.core.RequestUserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.hsuyeung.blog.constant.DateFormatConstants.FORMAT_YEAR_TO_SECOND;
import static com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.*;
import static com.hsuyeung.blog.constant.enums.ApiRateLimitStrategyEnum.USER;

/**
 * 请求频率限制切面
 *
 * @author hsuyeung
 * @date 2022/06/17
 */
@Component
@Aspect
@Profile({"dev", "prod"})
@Slf4j
@RequiredArgsConstructor
public class ApiRateLimitAspect {
    private final RedisUtil redisUtil;
    private final ISystemConfigService systemConfigService;

    /**
     * 以自定义 @ApiRateLimit 注解为切点
     */
    @Pointcut("@annotation(com.hsuyeung.blog.annotation.ApiRateLimit)")
    public void requestRateLimit() {
        // do nothing
    }

    /**
     * 在切点之前织入
     */
    @Before("requestRateLimit() && @annotation(apiRateLimit)")
    public void doBefore(JoinPoint joinPoint, ApiRateLimit apiRateLimit) {
        if (apiRateLimit.value() == 0) {
            return;
        }
        if (Objects.equals(apiRateLimit.strategy(), USER)) {
            String currentUid = String.valueOf(RequestUserHolder.getCurrentUid());
            this.checkApiRate(joinPoint, apiRateLimit, currentUid, REDIS_API_RATE_LIMIT_BY_UID_KEY);
        } else {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            AssertUtil.notNull(attributes, "attributes 不能为 null");
            String ipAddr = IpUtil.getIpAddr(attributes.getRequest());
            this.checkApiRate(joinPoint, apiRateLimit, ipAddr, REDIS_API_RATE_LIMIT_BY_IP_KEY);
        }
    }

    /**
     * 检查接口请求速率
     *
     * @param joinPoint        切点
     * @param apiRateLimit     限流注解
     * @param field            ip 地址或用户 id
     * @param systemConfigEnum 系统配置的枚举，ip 或用户 id
     */
    private void checkApiRate(JoinPoint joinPoint, ApiRateLimit apiRateLimit, String field,
                              SystemConfigConstants.SystemConfigEnum systemConfigEnum) {
        String key = String.format("%s:%s:%s",
                systemConfigService.getConfigValue(systemConfigEnum, String.class),
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
        String nextAvailableTimeStr = (String) redisUtil.hGet(key, field);
        LocalDateTime nextAvailableTime;
        LocalDateTime now = LocalDateTime.now();
        if (StringUtils.hasLength(nextAvailableTimeStr)) {
            nextAvailableTime = DateUtil.parseToGetLocalDateTime(nextAvailableTimeStr, FORMAT_YEAR_TO_SECOND);
            if (nextAvailableTime.isAfter(now)) {
                String message = StringUtils.hasText(apiRateLimit.message())
                        ? apiRateLimit.message()
                        : systemConfigService.getConfigValue(SYSTEM_API_RATE_LIMIT_EXCEPTION_TIP, String.class);
                throw new ApiRateLimitException(message
                        .replace("{value}", String.valueOf(apiRateLimit.value()))
                        .replace("{rest}", String.valueOf(DateUtil.getDiff(now, nextAvailableTime, apiRateLimit.timeUnit()))));
            }
        }
        nextAvailableTime = now.plus(apiRateLimit.value(), apiRateLimit.timeUnit());
        redisUtil.hPut(key, field, DateUtil.formatLocalDateTime(nextAvailableTime, FORMAT_YEAR_TO_SECOND));
    }
}
