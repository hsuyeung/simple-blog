package com.hsuyeung.blog.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger 接口文档配置
 *
 * @author hsuyeung
 * @date 2022/05/12
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
public class Swagger2Config {
    @Bean(value = "swaggerApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("博客管理后台接口 api 文档")
                        .description("博客管理后台接口 api 详细参数说明")
                        .contact(new Contact("hsuyeung", "hsuyeung.com", "hsuyeung.com@gmail.com"))
                        .version("1.0")
                        .build())
                .groupName("博客管理后台接口文档")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hsuyeung.blog.web.api"))
                .paths(PathSelectors.any())
                .build();
    }
}
