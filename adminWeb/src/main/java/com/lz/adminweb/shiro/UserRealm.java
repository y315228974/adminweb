package com.lz.adminweb.shiro;

import com.lz.adminweb.domain.AdminUser;
import com.lz.adminweb.service.AdminUserService;
import com.lz.adminweb.utils.MD5Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * UserRealm
 *
 * @author comtu
 * @version 1.0
 * @date 2020/6/15  18:22
 */
@Component
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private AdminUserService adminUserService;

    /**
     * 登录信息和用户验证信息验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String idCode = token.getUsername();
        String pwd = String.valueOf(token.getPassword());
        AdminUser user = adminUserService.getUserInfo(idCode);
        if (null == user) {
            throw new AuthenticationException("账号或密码错误");
        }
        if (!MD5Util.md5WithSalt(pwd).equals(user.getPasswd())) {
            throw new AuthenticationException("账号或密码错误");
        }
        final ShiroUser shiroUser = new ShiroUser();
        shiroUser.setId(user.getId());
        shiroUser.setUserName(user.getUserName());
        shiroUser.setMobile(user.getMobile());
        shiroUser.setRoleType(user.getRoleType());
        shiroUser.setLoginType(LoginType.ACCOUNT);
        return new SimpleAuthenticationInfo(shiroUser, pwd, getName());
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用,负责在应用程序中决定用户的访问控制的方法
     *
     * @param principals 主体
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//        authorizationInfo.setRoles(userInfoService.getUserRoles(shiroUser.getId()));
//        authorizationInfo.setStringPermissions(userInfoService.getUserPermissions(shiroUser.getId()));
        return authorizationInfo;
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        //获取当前主体对象
        PrincipalCollection principal = SecurityUtils.getSubject().getPrincipals();
        //清除缓存
        super.clearCache(principal);
    }
}
