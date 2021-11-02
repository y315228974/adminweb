package com.lz.adminweb.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

/**
 * shiro用户帮助类
 * @author comtu
 * @version 1.0
 * @date 2020/6/15  18:22
 */
@Component
public class ShiroUserUtil {

    /**
     * 获取shiro用户信息
     */
    public static ShiroUser getUserInfo() {
        ShiroUser user = null;
        Subject currentUser = SecurityUtils.getSubject();
        if (null != currentUser) {
            Session session = currentUser.getSession();
            if (null != session) {
                user = (ShiroUser) session.getAttribute("shiroUserAdmin");
            }
        }
        return user;
    }

    /**
     * 获取用户id
     */
    public static long getUserId(){
        ShiroUser shiroUser = getUserInfo();
        if(null == shiroUser){
            return 0;
        }
        return shiroUser.getId();
    }

    /**
     * 获取用户名称
     */
    public static String getUserName(){
        ShiroUser shiroUser = getUserInfo();
        if(null == shiroUser){
            return "";
        }
        return shiroUser.getUserName();
    }

}
