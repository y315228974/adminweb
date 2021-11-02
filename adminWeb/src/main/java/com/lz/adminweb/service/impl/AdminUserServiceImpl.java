package com.lz.adminweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lz.adminweb.constant.Configs;
import com.lz.adminweb.domain.AdminUser;
import com.lz.adminweb.domain.Cache.LoginErrorTimes;
import com.lz.adminweb.exception.ConsciousException;
import com.lz.adminweb.mapper.AdminUserMapper;
import com.lz.adminweb.redis.RedisUtil;
import com.lz.adminweb.service.AdminUserService;
import com.lz.adminweb.shiro.CustomizedToken;
import com.lz.adminweb.shiro.LoginType;
import com.lz.adminweb.shiro.ShiroUser;
import com.lz.adminweb.utils.MD5Util;
import com.lz.adminweb.utils.RandomValidateCodeUtil;
import com.lz.adminweb.utils.Validate.CaptchaShapeEnum;
import com.lz.adminweb.utils.Validate.ValidateProperty;
import com.lz.adminweb.utils.Validate.ValidateUtil;
import com.lz.adminweb.vo.JsonResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户表服务
 *
 * @author yaoyanhua
 * @date 2021-01-21 11:47:39
 */
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    @Value("${session.expiration}")
    private long expiration;

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 登录
     *
     * @param base64Mobile base64手机号码
     * @param base64Pwd    base64密码
     * @return com.yitu.hotel.vo.JsonResult
     * @author pangshihe
     * @date 2020/7/24 16:13
     */
    @Override
    public JsonResult<ShiroUser> login(String base64Mobile, String base64Pwd, String code, String tempTime) {
        if (StringUtils.isEmpty(base64Mobile)) {
            return JsonResult.fail("手机号码不能为空");
        }
        if (StringUtils.isEmpty(base64Pwd)) {
            return JsonResult.fail("密码不能为空");
        }
        if (!this.checkVerifyCode(code, tempTime)) {
            //return JsonResult.fail("验证码有误");
        }
        //String mobile = Base64Utils.decode(base64Mobile);
        //String pwd = Base64Utils.decode(base64Pwd);
        String mobile = base64Mobile;
        String pwd = base64Pwd;
        if (StringUtils.isEmpty(mobile)) {
            return JsonResult.fail("手机号码错误");
        }
        if (StringUtils.isEmpty(mobile)) {
            return JsonResult.fail("密码错误");
        }
        return toLogin(pwd, mobile);
    }

    /**
     * 校验验证码是否正确
     * @author: luzhichao
     * @param code
     * @param tempTime
     * @return: void
     * @date: 2021/10/29 16:13
     */
    private boolean checkVerifyCode(String code, String tempTime) {
        Object verifyCodeObj = redisUtil.get(Configs.REDIS_KAPTCHA_TEMPTIME + tempTime);
        if (verifyCodeObj == null) {
            return false;
        }
        String verifyCode = verifyCodeObj != null ? verifyCodeObj.toString() : null;
        if (!StringUtils.equalsIgnoreCase(verifyCode, code)) {
            return false;
        }
        return true;
    }

    /**
     * 登出
     *
     * @return com.yitu.hotel.vo.JsonResult
     * @author pangshihe
     * @date 2020/7/25 1:36
     */
    @Override
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        return "login";
    }

    /**
     * 登录封装
     *
     * @param pwd    密码
     * @param mobile 手机号码
     * @return com.yitu.hotel.vo.JsonResult
     * @author yaoyanhua
     * @date 2021/1/23
     */
    private JsonResult<ShiroUser> toLogin(String pwd, String mobile) {
        AdminUser adminUser = getUserInfo(mobile);
        if (adminUser == null) {
            return JsonResult.fail("账号不存在");
        }
        final int MAX_TIMES = 5;
        LoginErrorTimes loginErrorTimes = getLoginErrorTimes(mobile);
        if (loginErrorTimes != null) {
            long diff = System.currentTimeMillis() - loginErrorTimes.getTryTimestamp();
            if (loginErrorTimes.getTryCount() >= MAX_TIMES) {
                if ((diff / 1000 / 60 / 60) < 1) {
                    return JsonResult.fail("重试次数过多，冻结1小时");
                } else {
                    //时间超过一小时解除重试
                    loginErrorTimes.setTryCount(0);
                    loginErrorTimes.setTryTimestamp(System.currentTimeMillis());
                }
            }
        }
        if (!MD5Util.md5WithSalt(pwd).equals(adminUser.getPasswd())) {
            if (loginErrorTimes == null) {
                loginErrorTimes = new LoginErrorTimes();
                loginErrorTimes.setTryCount(1);
                loginErrorTimes.setTryTimestamp(System.currentTimeMillis());
            } else {
                loginErrorTimes.setTryCount(loginErrorTimes.getTryCount() + 1);
                loginErrorTimes.setTryTimestamp(System.currentTimeMillis());
            }
            updateLoginErrorTimes(mobile, loginErrorTimes);
            int count = MAX_TIMES - loginErrorTimes.getTryCount();
            return JsonResult.fail("手机号或密码错误，剩余重试次数" + count + "次");
        }

        try {
            Subject subject = SecurityUtils.getSubject();
            CustomizedToken customizedToken = new CustomizedToken(mobile, pwd, LoginType.ACCOUNT.toString());
            customizedToken.setRememberMe(false);
            subject.login(customizedToken);
            if (subject.isAuthenticated()) {
                ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
                subject.getSession().setAttribute("shiroUserAdmin", shiroUser);
                subject.getSession().setTimeout(expiration);
                shiroUser.setToken(subject.getSession().getId());
                //TODO 更新最后登录时间
                //userMapper.updateLastLoginDate(new Date(), shiroUser.getId());

//                String redirect_uri = "";
//                SavedRequest savedRequest = WebUtils.getSavedRequest(request);
//                // 获取保存的URL
//                if (!(savedRequest == null || savedRequest.getRequestUrl() == null)) {
//                    redirect_uri = savedRequest.getRequestUrl();
//                }
                return JsonResult.ok(shiroUser);
            }
        } catch (AuthenticationException ex) {
            throw new ConsciousException(ex.getMessage());
        }
        return JsonResult.fail("登录失败");
    }

    /**
     * 获取登录错误次数
     *
     * @param mobilePhone 手机号码
     * @return com.yitu.hoteladmin.model.LoginErrorTimes
     * @author pangshihe
     * @date 2020/9/23 11:30
     */
    private LoginErrorTimes getLoginErrorTimes(String mobilePhone) {
        Object object = redisUtil.get(mobilePhone);
        if (object != null) {
            Gson gson = new Gson();
            try {
                LoginErrorTimes loginErrorTimes = gson.fromJson(object.toString(), new TypeToken<LoginErrorTimes>() {
                }.getType());
                return loginErrorTimes;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 更新登录错误次数
     *
     * @param mobilePhone     手机号码
     * @param loginErrorTimes 登录次数实体
     * @return com.yitu.hoteladmin.model.LoginErrorTimes
     * @author pangshihe
     * @date 2020/9/23 11:30
     */
    private int updateLoginErrorTimes(String mobilePhone, LoginErrorTimes loginErrorTimes) {
        Gson gson = new Gson();
        redisUtil.del(mobilePhone);
        redisUtil.set(mobilePhone, gson.toJson(loginErrorTimes));
        return 1;
    }

    /**
     * 修改密码
     *
     * @param userId 用户id
     * @param pwd    密码
     * @return com.yitu.hoteladmin.entity.JsonResult
     * @author pangshihe
     * @date 2020/8/4 16:51
     */
    @Override
    public JsonResult changePwd(long userId, String pwd) {
        if (StringUtils.isEmpty(pwd)) {
            return JsonResult.fail("密码不能为空");
        }
        AdminUser adminUsers = adminUserMapper.selectById(userId);
        if (adminUsers == null || adminUsers.getDeleted() == 1) {
            return JsonResult.fail("用户不存在或已禁用");
        }
        adminUsers.setPasswd(MD5Util.md5WithSalt(pwd));
        int result = adminUserMapper.updateById(adminUsers);
        return result > 0 ? JsonResult.ok("密码修改成功", null) : JsonResult.fail("修改失败");
    }

    /**
     * 获取用户
     *
     * @param mobile 手机号
     * @return com.yitu.hotel.domain.User
     * @author yaoyanhua
     * @date 2020/6/23 17:53
     */
    @Override
    public AdminUser getUserInfo(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return null;
        }
        List<AdminUser> adminUsers = adminUserMapper.selectList(
                new LambdaQueryWrapper<AdminUser>()
                        .eq(AdminUser::getMobile, mobile)
                        .eq(AdminUser::getDeleted, 0));
        if (adminUsers == null || adminUsers.isEmpty()) {
            return null;
        }
        return adminUsers.get(0);
    }

    /**
     * 生成验证码
     * @author: luzhichao
     * @return: com.yitu.gmq.wygl.admin.api.vo.JsonResult
     * @date: 2021/7/16 10:59
     */
    @Override
    public JsonResult<Map<String, Object>> getVerifyCode() {
        ValidateProperty image = ValidateUtil.createImage(4, CaptchaShapeEnum.LINE);
        String tempTime = System.currentTimeMillis() + new RandomValidateCodeUtil().generateCode(4);
        redisUtil.set(Configs.REDIS_KAPTCHA_TEMPTIME + tempTime, image.getCode(), 120);
        Map<String, Object> map = new HashMap<>();
        map.put("base64Img", image.getImageBase64Data());
        map.put("tempTime", tempTime);
        return JsonResult.ok(map);
    }
}
