package com.lz.adminweb.config;

import com.lz.adminweb.domain.FileConfigEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 登录验证和权限验证拦截器配置
 * 注释掉@Configuration关闭配置
 * @date 2020/6/22 14:12
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private FileConfigEntity fileConfigEntity;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        if (fileConfigEntity.getMappingSwitch()) {
            //配置server虚拟路径，handler为jsp中访问的目录，locations为files相对应的本地路径
            registry.addResourceHandler("/files/**").addResourceLocations("file:/" + fileConfigEntity.getUrl());
        }
    }

    /**
     * Add Spring MVC lifecycle interceptors for pre- and post-processing of
     * controller method invocations and resource handler requests.
     * Interceptors can be registered to apply to all requests or be limited
     * to a subset of URL patterns.
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //RestRequestHandler restRequestHandler = new RestRequestHandler();
        //registry.addInterceptor(restRequestHandler).addPathPatterns("/rest/**").excludePathPatterns("/login/**");
    }
}