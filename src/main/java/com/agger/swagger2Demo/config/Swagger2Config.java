package com.agger.swagger2Demo.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @program: Swagger2Config
 * @description: TODO
 * @author: chenhx
 * @create: 2019-11-25 14:14
 **/
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Value("${swagger2.enable}")
    private boolean enable;

    @Bean("userApis")
    public Docket userApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户模块")      // 分组
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.regex("/user.*"))
                .build()
                .apiInfo(apiInfo())
                .enable(enable);   // 是否开启API
    }


    @Bean("deptApis")
    public Docket deptApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("部门模块")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.regex("/dept.*"))
                .build()
                .apiInfo(apiInfo())
                .enable(enable);
    }


    /**
     * 编写apiInfo配置
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API接口接口文档测试项目")
                .description("描述：编写一个基于springBoot的swagger2接口文档测试demo")
                .contact(new Contact("agger",null,null))
                .version("1.0.0")
                .build();
    }
}
