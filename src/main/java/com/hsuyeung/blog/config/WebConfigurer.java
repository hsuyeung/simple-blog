package com.hsuyeung.blog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsuyeung.blog.config.properties.SecurityProperties;
import com.hsuyeung.blog.interceptor.UserPermissionCheckInterceptor;
import com.hsuyeung.blog.interceptor.UserTokenCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * 扩展 SpringBoot 相关的配置
 *
 * @author hsuyeung
 * @date 2022-02-24
 */
@EnableWebMvc
@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    @Resource
    private UserPermissionCheckInterceptor userPermissionCheckInterceptor;
    @Resource
    private UserTokenCheckInterceptor userTokenCheckInterceptor;
    @Resource(name = "securityProperties")
    private SecurityProperties properties;
    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 放行 knife4j 的静态资源
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePathPatterns = properties.getExcludePathPatterns();
        List<String> pathPatterns = properties.getPathPatterns();
        registry.addInterceptor(userTokenCheckInterceptor)
                .addPathPatterns(pathPatterns)
                .excludePathPatterns(excludePathPatterns)
                .order(0);
        registry.addInterceptor(userPermissionCheckInterceptor)
                .addPathPatterns(pathPatterns)
                .excludePathPatterns(excludePathPatterns)
                .order(1);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
    }
}
