package com.hsuyeung.blog.filter;

import com.hsuyeung.blog.config.properties.SecurityProperties;
import com.hsuyeung.blog.web.core.HeaderMapRequestWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author hsuyeung
 * @date 2022/12/23
 */
@Component
@RequiredArgsConstructor
public class AddHeaderFilter implements Filter {
    private final SecurityProperties properties;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String tokenName = properties.getTokenName();
        String token = req.getParameter(tokenName);
        if (StringUtils.hasText(token)) {
            HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(req);
            requestWrapper.addHeader(tokenName, token);
            filterChain.doFilter(requestWrapper, servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
