package com.hsuyeung.blog.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsuyeung.blog.service.IRbacAuthorityService;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * 用户权限检查拦截器
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserPermissionCheckInterceptor implements HandlerInterceptor, IBaseWebResponse {
    private final IRbacAuthorityService rbacAuthorityService;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean hasPermission = rbacAuthorityService.hasPermission(request);
        if (!hasPermission) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(err(FORBIDDEN.value(), "没有权限操作该资源")));
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
