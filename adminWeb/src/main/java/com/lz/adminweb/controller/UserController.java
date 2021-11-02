package com.lz.adminweb.controller;

import com.lz.adminweb.service.AdminUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pangshihe on 2019/4/24.
 */
@RestController
@RequestMapping(value = "user")
@Api(value = "用户管理", tags = {"用户管理"})
public class UserController {

    @Autowired
    AdminUserService adminUserService;

    //@RequestMapping(value = "saveUser", method = RequestMethod.POST)
    //public JsonResult saveUser(HttpServletRequest request,
    //                           @RequestParam(value = "userId", defaultValue = "0") int userId,
    //                           @NotBlank(message = "用户名不能为空") @Length(max = 50, message = "用户名不能超过50个字符")
    //                           @RequestParam(value = "userName", defaultValue = "") String userName,
    //                           @NotBlank(message = "账号不能为空") @Length(max = 50, message = "账号不能超过50个字符")
    //                           @RequestParam(value = "account", defaultValue = "") String account,
    //                           @Length(max = 18, message = "身份证不能超过18个字符")
    //                           @RequestParam(value = "userIdCard", defaultValue = "") String userIdCard,
    //                           @NotBlank(message = "手机号码不能为空")
    //                           @RequestParam(value = "mobilePhone", defaultValue = "") String mobilePhone,
    //                           @Min(value = 1, message = "公司错误")
    //                           @RequestParam(value = "companyId", defaultValue = "0") int companyId,
    //                           @Min(value = 0, message = "部门id错误")
    //                           @RequestParam(value = "departmentId", defaultValue = "0") int departmentId,
    //                           @RequestParam(value = "userType", defaultValue = "1") int userType,
    //                           @RequestParam(value = "certificationId", defaultValue = "0") int certificationId,
    //                           @RequestParam(value = "engId", defaultValue = "") String engId,
    //                           @RequestParam(value = "engName", defaultValue = "") String engName) {
    //    return userService.saveUser(request, userId, userName, userIdCard, mobilePhone, companyId, departmentId, userType,
    //            certificationId, account, engId, engName);
    //}
    //
    //@RequestMapping(value = "deleteUser", method = RequestMethod.POST)
    //public JsonResult deleteUser(HttpServletRequest request,
    //                             @Min(value = 1, message = "用户错误")
    //                             @RequestParam(value = "userId", defaultValue = "0") int userId) {
    //    return userService.deleteUser(request, userId);
    //}
    //
    //@RequestMapping(value = "getUserDetail", method = RequestMethod.POST)
    //public JsonResult getUserDetail(@Min(value = 1, message = "用户错误")
    //                                @RequestParam(value = "userId", defaultValue = "0") int userId) {
    //    return userService.getUserDetail(userId);
    //}
    //
    //@RequestMapping(value = "getUserList", method = RequestMethod.POST)
    //public JsonResult getUserList(HttpServletRequest request,
    //                              @RequestParam(value = "userType", defaultValue = "0") int userType,
    //                              @RequestParam(value = "keyword", defaultValue = "") String keyword,
    //                              @Min(value = 1, message = "页码错误")
    //                              @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
    //                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
    //    return userService.getUserList(request, userType, keyword, pageIndex, pageSize);
    //}
    //
    ///**
    // * 管理员修改用户密码
    // */
    //@RequestMapping(value = "changeUserPassword", method = RequestMethod.POST)
    //public JsonResult changeUserPassword(@Min(value = 1, message = "用户错误")
    //                                     @RequestParam(value = "userId", defaultValue = "0") int userId,
    //                                     @NotBlank(message = "密码不能为空")
    //                                     @RequestParam(value = "password", defaultValue = "") String password) {
    //    return userService.changeUserPassword(userId, password);
    //}
    //
    //@RequestMapping(value = "registerByMobilePhone", method = RequestMethod.POST)
    //public JsonResult registerByMobilePhone(@NotBlank(message = "手机号码不能为空") @Length(max = 11, message = "手机号码不能超过11个字符")
    //                                        @RequestParam(value = "mobilePhone", defaultValue = "") String mobilePhone,
    //                                        @NotBlank(message = "密码不能为空")
    //                                        @RequestParam(value = "password", defaultValue = "") String password,
    //                                        @NotBlank(message = "验证码不能为空")
    //                                        @RequestParam(value = "authCode", defaultValue = "") String authCode,
    //                                        @NotBlank(message = "身份证号码不能为空") @Length(min = 18, message = "身份证号码不正确")
    //                                        @RequestParam(value = "userIdCard", defaultValue = "") String userIdCard,
    //                                        @NotBlank(message = "姓名不能为空") @Length(max = 50, message = "姓名长度不正确")
    //                                        @RequestParam(value = "realName", defaultValue = "") String realName,
    //                                        @NotBlank(message = "身份证正面不能为空") @Length(max = 200, message = "身份证Url长度不能超过200个字符")
    //                                        @RequestParam(value = "idCardFrontUrl", defaultValue = "") String idCardFrontUrl,
    //                                        @NotBlank(message = "身份证反面不能为空") @Length(max = 200, message = "身份证Url长度不能超过200个字符")
    //                                        @RequestParam(value = "idCardReverseUrl", defaultValue = "") String idCardReverseUrl) {
    //    return userService.registerByMobilePhone(mobilePhone, password, authCode, userIdCard, realName, idCardFrontUrl, idCardReverseUrl);
    //}
    //
    //@RequestMapping(value = "changePassword", method = RequestMethod.POST)
    //public JsonResult changePassword(HttpServletRequest request,
    //                                 @NotBlank(message = "新密码不能为空")
    //                                 @RequestParam(value = "newPassword", defaultValue = "") String newPassword,
    //                                 @NotBlank(message = "旧密码不能为空")
    //                                 @RequestParam(value = "oldPassword", defaultValue = "") String oldPassword) {
    //    return userService.changePassword(request, newPassword, oldPassword);
    //}
    //
    ///**
    // * 修改密码（培训系统使用）
    // */
    //@RequestMapping(value = "changePasswordForOutside", method = RequestMethod.POST)
    //public JsonResult changePasswordForOutside(@Min(value = 1, message = "用户错误")
    //                                           @RequestParam(value = "userId", defaultValue = "0") int userId,
    //                                           @NotBlank(message = "新密码不能为空")
    //                                           @RequestParam(value = "newPassword", defaultValue = "") String newPassword,
    //                                           @NotBlank(message = "旧密码不能为空")
    //                                           @RequestParam(value = "oldPassword", defaultValue = "") String oldPassword) {
    //    return userService.changePasswordForOutside(userId, newPassword, oldPassword);
    //}
    //
    ///**
    // * 重置密码(目前只有手机端使用)
    // */
    //@RequestMapping(value = "resetPassword", method = RequestMethod.POST)
    //public JsonResult resetPassword(@NotBlank(message = "手机号码不能为空") @Length(max = 11, message = "手机号码不能超过11个字符")
    //                                @RequestParam(value = "mobilePhone", defaultValue = "") String mobilePhone,
    //                                @NotBlank(message = "密码不能为空")
    //                                @RequestParam(value = "password", defaultValue = "") String password,
    //                                @NotBlank(message = "验证码不能为空")
    //                                @RequestParam(value = "authCode", defaultValue = "") String authCode) {
    //    return userService.resetPassword(mobilePhone, password, authCode);
    //}
    //
    //@RequestMapping(value = "getCertificationList", method = RequestMethod.POST)
    //public JsonResult getCertificationList() {
    //    return userService.getCertificationList();
    //}
    //
    //@RequestMapping(value = "getUserListByDepartmentId", method = RequestMethod.POST)
    //public JsonResult getUserListByDepartmentId(HttpServletRequest request,
    //                                            @Min(value = 1, message = "部门id错误")
    //                                            @RequestParam(value = "departmentId", defaultValue = "0") int departmentId,
    //                                            @Min(value = 1, message = "页码错误")
    //                                            @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
    //                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
    //    return userService.getUserListByDepartmentId(request, departmentId, pageIndex, pageSize);
    //}
}
