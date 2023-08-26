package com.hsuyeung.blog.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsuyeung.blog.config.properties.SecurityProperties;
import com.hsuyeung.blog.service.IUserTokenService;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.RequestUserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * 校验用户 token 是否有效的拦截器
 *
 * @author hsuyeung
 * @date 2022/06/29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserTokenCheckInterceptor implements HandlerInterceptor, IBaseWebResponse {
    private final IUserTokenService userTokenService;
    private final ObjectMapper objectMapper;
    private final SecurityProperties properties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        // 从 header 里取出 token 并校验
        String token = request.getHeader(properties.getTokenName());
        if (!StringUtils.hasLength(token)) {
            response.getWriter().write(objectMapper.writeValueAsString(err(UNAUTHORIZED.value(), "未登录，请先登录")));
            return false;
        }
        if (userTokenService.isExpired(token)) {
            response.getWriter().write(objectMapper.writeValueAsString(err(UNAUTHORIZED.value(), "token 无效或已过期，请重新登录")));
            return false;
        }
        // 记录当前操作人
        // 由于 UserTokenCheckInterceptor 和 UserPermissionCheckInterceptor 拦截和放行的路径是完全一样的，
        // 所以只需要在这里记录操作人即可，不需要两个拦截器都配置
        RequestUserHolder.addCurrentUid(userTokenService.getUserIdFromRequestHeader(request));
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 拦截器之后记得 remove，避免 OOM
        RequestUserHolder.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
