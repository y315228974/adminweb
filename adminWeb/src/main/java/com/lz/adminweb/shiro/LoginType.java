package com.lz.adminweb.shiro;

/**
 * 登录类型
 * @author comtu
 * @version 1.0
 * @date 2020/6/15  18:22
 */
public enum LoginType {
    ACCOUNT("account"); //账号密码

    private String type;

    private LoginType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}
