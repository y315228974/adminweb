package com.lz.adminweb.config;

import com.lz.adminweb.filter.RefererFilter;
import com.lz.adminweb.filter.XssAndSqlFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器配置
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean RefererFilterRegistration(){
        FilterRegistrationBean<RefererFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new RefererFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("RefererFilter");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean XssAndSqlFilterRegistration(){
        FilterRegistrationBean<XssAndSqlFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new XssAndSqlFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("XssAndSqlFilter");
        filterRegistrationBean.setOrder(2);
        return filterRegistrationBean;
    }
}
