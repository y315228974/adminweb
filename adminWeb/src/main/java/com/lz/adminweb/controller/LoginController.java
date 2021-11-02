package com.lz.adminweb.controller;

import com.lz.adminweb.service.AdminUserService;
import com.lz.adminweb.shiro.ShiroUser;
import com.lz.adminweb.vo.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 登录
 *
 * @author yaoyanhua
 * @version 1.0
 * @date 2021/1/23
 */
@RestController
@Api(value = "登录模块", tags = "登录模块")
public class LoginController {

    @Autowired
    private AdminUserService adminUserService;

    @PostMapping(value = "/login")
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "账号", dataType = "String", required = true),
            @ApiImplicitParam(name = "pwd", value = "密码", dataType = "String", required = true),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "String", required = true),
            @ApiImplicitParam(name = "tempTime", value = "时间搓", dataType = "String", required = true),
    })
    public JsonResult<ShiroUser> login(@RequestParam("mobile") String mobile,
                                       @RequestParam("pwd") String pwd,
                                       @RequestParam("code") String code,
                                       @RequestParam("tempTime") String tempTime) {
        return adminUserService.login(mobile, pwd, code, tempTime);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "登出")
    public String logout() {
        return adminUserService.logout();
    }

    /***
     *
     * @author: luzhichao
     * @return: com.lz.adminweb.vo.JsonResult<java.util.Map<java.lang.String,java.lang.Object>>
     * @date: 2021/10/29 16:26
     */
    @PostMapping("/getVerifyCode")
    @ApiOperation(value = "获取验证码")
    public JsonResult<Map<String, Object>> getVerifyCode() {
        return adminUserService.getVerifyCode();
    }
}
