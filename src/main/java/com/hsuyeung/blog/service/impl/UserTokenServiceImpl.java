package com.hsuyeung.blog.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.hsuyeung.blog.config.properties.SecurityProperties;
import com.hsuyeung.blog.service.ISystemConfigService;
import com.hsuyeung.blog.service.IUserTokenService;
import com.hsuyeung.blog.util.AssertUtil;
import com.hsuyeung.blog.util.DateUtil;
import com.hsuyeung.blog.util.JwtUtil;
import com.hsuyeung.blog.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.REDIS_USER_TOKEN_KEY;
import static com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.USER_TOKEN_SECRET;


/**
 * 用户 token 相关服务实现类
 *
 * @author hsuyeung
 * @date 2022/05/19
 */
@Slf4j
@Service("userTokenService")
@RequiredArgsConstructor
public class UserTokenServiceImpl implements IUserTokenService {
    private final RedisUtil redisUtil;
    private final ISystemConfigService systemConfigService;
    private final SecurityProperties properties;


    @Override
    public String generateUserToken(Long userId, LocalDateTime expiresAt) {
        Map<String, Object> headerMap = new HashMap<>(2);
        headerMap.put("alg", "HS256");
        headerMap.put("typ", "JWT");
        String userTokenKey = systemConfigService.getConfigValue(REDIS_USER_TOKEN_KEY, String.class);
        redisUtil.hDelete(userTokenKey, String.valueOf(userId));
        JWTCreator.Builder builder = JWT.create()
                .withHeader(headerMap)
                .withClaim("id", userId)
                .withIssuedAt(DateUtil.fromJava8LocalDateToDate(LocalDateTime.now()));
        if (Objects.nonNull(expiresAt)) {
            builder.withExpiresAt(DateUtil.fromJava8LocalDateToDate(expiresAt));
        }
        String token = builder.sign(Algorithm.HMAC256(systemConfigService.getConfigValue(USER_TOKEN_SECRET, String.class)));
        redisUtil.hPut(userTokenKey, String.valueOf(userId), token);
        log.info("生成用户 {} 的 token: {} 成功", userId, token);
        return token;
    }

    @Override
    public boolean isExpired(String token) {
        if (!StringUtils.hasLength(token)) {
            return true;
        }
        Map<String, Claim> claimMap = JwtUtil.getClaims(token, systemConfigService.getConfigValue(USER_TOKEN_SECRET, String.class));
        // 如果解析 token 失败则认为 token 已经过期
        if (CollectionUtils.isEmpty(claimMap)) {
            return true;
        }
        // 获取用户 id
        Claim idClaim = claimMap.get("id");
        // 如果没有获取到用户的 id 则认为 token 过期
        if (Objects.isNull(idClaim)) {
            return true;
        }
        // 如果获取到了用户 id，则根据用户 id 去 redis 中查询其 token
        String redisToken = (String) redisUtil.hGet(
                systemConfigService.getConfigValue(REDIS_USER_TOKEN_KEY, String.class),
                String.valueOf(idClaim.asInt()));
        // 如果 redis 中不存在则说明 token 已经过期
        if (!StringUtils.hasLength(redisToken)) {
            return true;
        } else {
            // 如果 redis 里存在该 token 但是和传入的 token不一样那也是过期了的
            return !redisToken.equals(token);
        }
    }

    @Override
    public boolean deleteUserToken(Long uid) {
        String userTokenKey = systemConfigService.getConfigValue(REDIS_USER_TOKEN_KEY, String.class);
        String token = (String) redisUtil.hGet(userTokenKey, String.valueOf(uid));
        if (!StringUtils.hasLength(token)) {
            log.warn("用户 {} 的 token 已过期，无需手动删除", uid);
            return true;
        }
        return redisUtil.hDelete(userTokenKey, String.valueOf(uid)) > 0;
    }

    @Override
    public Long getUserIdFromRequestHeader(HttpServletRequest request) {
        String token = request.getHeader(properties.getTokenName());
        AssertUtil.hasLength(token, "token 获取失败");
        return JwtUtil.getClaim(token, "id", Long.class, systemConfigService.getConfigValue(USER_TOKEN_SECRET, String.class));
    }
}
