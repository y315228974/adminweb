package com.lz.adminweb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.adminweb.domain.AdminUser;
import com.lz.adminweb.shiro.ShiroUser;
import com.lz.adminweb.vo.JsonResult;

import java.util.Map;

/**
 * 用户表服务
 * @author yaoyanhua
 * @date 2021-01-21 11:47:39
 */
public interface AdminUserService extends IService<AdminUser> {
    /**
     * 登录
     *
     * @param base64Mobile base64手机号码
     * @param base64Pwd    base64密码
     * @return com.yitu.hotel.vo.JsonResult
     * @author pangshihe
     * @date 2020/7/24 16:13
     */
    JsonResult<ShiroUser> login(String base64Mobile, String base64Pwd, String code, String tempTime);

    /**
     * 登出
     *
     * @return com.yitu.hotel.vo.JsonResult
     * @author pangshihe
     * @date 2020/7/25 1:36
     */
    String logout();

    /**
     * 修改密码
     *
     * @param userId 用户id
     * @param pwd    密码
     * @return com.yitu.hoteladmin.entity.JsonResult
     * @author pangshihe
     * @date 2020/8/4 16:51
     */
    JsonResult changePwd(long userId, String pwd);

    /**
     * 获取用户
     *
     * @param mobile 手机号
     * @return com.yitu.hotel.domain.User
     * @author yaoyanhua
     * @date 2020/6/23 17:53
     */
    AdminUser getUserInfo(String mobile);

    /**
     * 获取校验码
     * @author: luzhichao
     * @return: void
     * @date: 2021/7/16 10:50
     */
    JsonResult<Map<String, Object>> getVerifyCode();
}
