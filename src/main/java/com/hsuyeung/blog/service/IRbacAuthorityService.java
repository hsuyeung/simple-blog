package com.hsuyeung.blog.service;

import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * RBAC 认证服务接口
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
@Validated
public interface IRbacAuthorityService {
    /**
     * 判断当前请求的用户是否有权限
     *
     * @param request {@link HttpServletRequest}
     * @return 有权限返回 true，无权限返回 false
     */
    boolean hasPermission(@NotNull(message = "request 不能为 null") HttpServletRequest request);

    /**
     * 根据 token 判断是否有权限
     *
     * @param request {@link HttpServletRequest}
     * @param token   token
     * @return 有权限返回 true，无权限返回 false
     */
    boolean hasPermission(@NotNull(message = "request 不能为 null") HttpServletRequest request,
                          @NotBlank(message = "token 不能为空") String token);
}
