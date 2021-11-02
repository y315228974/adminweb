package com.lz.adminweb.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 自定义token
 * @author comtu
 * @version 1.0
 * @date 2020/6/15  18:22
 */
public class CustomizedToken extends UsernamePasswordToken {

    /**
     * 登录类型
     */
    private String loginType;

    public CustomizedToken(final String username, final String password,String loginType) {
        super(username,password);
        this.loginType = loginType;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
