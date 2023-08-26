package com.hsuyeung.blog.service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 用户 token 相关服务
 *
 * @author hsuyeung
 * @date 2022/05/19
 */
public interface IUserTokenService {

    /**
     * 生成用户相关 token，并将其存入 redis 的 hash 结构中
     * <p>目前的登录策略：每访问一次登录接口就将之前的 token 删除然后存入新的 token</p>
     *
     * @param userId    用户 id
     * @param expiresAt 该 token 在什么时候过期
     * @return 生成的 token
     */
    String generateUserToken(Long userId, LocalDateTime expiresAt);

    /**
     * 判断用户 token 是否过期
     *
     * @param token 用户 token
     * @return 如果 token 解析失败、已经到达过期时间、redis 中不存在该用户的 token 或是 redis 中的 token 与传入的 token 不相等则返回 true，否则返回 false
     */
    boolean isExpired(String token);

    /**
     * 删除指定用户 token
     *
     * @param uid 用户 id
     * @return 删除成功返回 true，否则返回 false
     */
    boolean deleteUserToken(Long uid);

    /**
     * 从 request 的 header 中获取 token 中的用户 id
     *
     * @param request {@link HttpServletRequest}
     * @return 用户 id
     */
    Long getUserIdFromRequestHeader(@NotNull(message = "request 不能为 null") HttpServletRequest request);
}
