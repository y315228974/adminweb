package com.lz.adminweb.config;

import com.lz.adminweb.constant.Configs;
import com.lz.adminweb.redis.RedisConfig;
import com.lz.adminweb.shiro.*;
import lombok.Data;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shiro配置类
 * @version 1.0
 * @date 2020/6/11
 */
@Configuration
@AutoConfigureAfter(RedisConfig.class)
@Data
public class ShiroConfig {

    @Value("${spring.profiles.active}")
    private String active;

    @Resource
    private RedisManager redisManager;

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        /* 自定义filter注册 */
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("logout", new CustomLogoutFilter());

        //shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/");
        //无权限页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noPermission");

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //静态资源
        filterChainDefinitionMap.put("/file/**", "anon");
        filterChainDefinitionMap.put("/files/**", "anon");
        filterChainDefinitionMap.put("/lib/**", "anon");
        filterChainDefinitionMap.put("/error", "anon");
        filterChainDefinitionMap.put("/noPermission", "anon");
        filterChainDefinitionMap.put("/captcha.jpg", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        //登录
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/getVerifyCode", "anon");
        //swagger
        filterChainDefinitionMap.put("/doc.html", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        //druid
        filterChainDefinitionMap.put("/druid/**", "anon");

        filterChainDefinitionMap.put("/api/**", "anon");
        filterChainDefinitionMap.put("/statistic/**", "anon");

        //健康监控
        //filterChainDefinitionMap.put("/health/**", "anon");

        //rest
        filterChainDefinitionMap.put("/rest/**", "anon");

        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 获取realm
     * @param
     * @return com.yitu.hotel.shiro.UserRealm
     * @author yaoyanhua
     * @date 2020/6/23 11:48
     */
    @Bean
    public UserRealm getUserRealm() {
        UserRealm realm = new UserRealm();
        realm.setName(LoginType.ACCOUNT.toString());
        return realm;
    }

    /**
     * securityManager
     * @param
     * @return org.apache.shiro.mgt.SecurityManager
     * @author yaoyanhua
     * @date 2020/6/23 11:48
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(modularRealmAuthenticator());
        List<Realm> realms = new ArrayList<>();
        realms.add(getUserRealm());
        securityManager.setRealms(realms);
        //自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        //自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

//    /**
//     * RedisManager
//     * @param
//     * @return org.crazycake.shiro.RedisManager
//     * @author yaoyanhua
//     * @date 2020/8/5 16:54
//     */
//    private RedisManager redisManager() {
//        RedisManager redisManager = new RedisManager();
//        redisManager.setHost(host);
//        redisManager.setPort(port);
//        redisManager.setTimeout(timeout);
//        redisManager.setPassword(password);
//        redisManager.setDatabase(database);
//        return redisManager;
//    }

    /**
     * cacheManager 缓存 redis实现, 使用的是shiro-redis开源插件
     * @param
     * @return RedisCacheManager
     * @author yaoyanhua
     * @date 2020/8/5 14:37
     */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        // 必须要设置主键名称，shiro-redis 插件用过这个缓存用户信息
        redisCacheManager.setPrincipalIdFieldName("id");
        return redisCacheManager;
    }


    /**
     * 定义会话管理器的操作
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        CustomDefaultWebSessionManager sessionManager = new CustomDefaultWebSessionManager();
        //定义的是全局的session会话超时时间，此操作会覆盖web.xml文件中的超时时间配置，单位毫秒
        sessionManager.setGlobalSessionTimeout(100 * 60 * Configs.REDIS_SESSION_TIME_OUT);//30分钟
        //删除所有无效的Session对象，此时的session被保存在了内存里面
        sessionManager.setDeleteInvalidSessions(true);
        //定义Session可以进行序列化的工具类
        sessionManager.setSessionDAO(redisSessionDAO());
        //所有的session一定要将id设置到Cookie之中，需要提供有Cookie的操作模版
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * sessionDAO持久化
     * @param
     * @author yaoyanhua
     * @date 2020/8/5 17:00
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager);
        redisSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        //单位秒，超过多少秒无操作redis过期
        //Please make sure expire is longer than sesion.getTimeout
        //redisSessionDAO.setExpire(30000000);//1年,比session设大大点
        return redisSessionDAO;
    }

    /**
     * 定义Session ID生成管理器
     * @param
     * @return org.apache.shiro.session.mgt.eis.SessionIdGenerator
     * @author yaoyanhua
     * @date 2020/8/5 13:04
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    @Bean
    public SimpleCookie sessionIdCookie() {
        //在Tomcat运行下默认使用的Cookie的名字为JSESSIONID
        SimpleCookie sessionIdCookie = new SimpleCookie("admin-session-id");
        //保证该系统不会受到跨域的脚本操作供给
        sessionIdCookie.setHttpOnly(true);
        //定义Cookie的过期时间，单位为秒，如果设置为-1表示浏览器关闭，则Cookie消失
        if("prod".equals(active)) {
            sessionIdCookie.setSecure(true);
        }
        //有效期-1表示跟session,session全局设置可以通过sessionManager.setGlobalSessionTimeout
        sessionIdCookie.setMaxAge(15552000);//180天
        //path为 / 用于多个系统共享 JSESSIONID
        sessionIdCookie.setPath("/");
        return sessionIdCookie;
    }

    /**
     * 系统自带的Realm管理，主要针对多realm
     * @param
     * @return org.apache.shiro.authc.pam.ModularRealmAuthenticator
     * @author yaoyanhua
     * @date 2020/6/23 11:48
     */
    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator(){
        //自己重写的ModularRealmAuthenticator
        CustomizedModularRealmAuthenticator modularRealmAuthenticator = new CustomizedModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }

    /**
     * authorizationAttributeSourceAdvisor
     * @param
     * @return org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
     * @author yaoyanhua
     * @date 2020/6/23 11:49
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

}