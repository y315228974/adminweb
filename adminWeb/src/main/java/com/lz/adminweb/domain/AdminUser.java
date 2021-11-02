package com.lz.adminweb.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 后台管理用户实体类
 *
 * @author yaoyanhua
 * @date 2020/9/29 14:38
 */
@Data
@TableName("system_user")
public class AdminUser {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 用户密码
     */
    private String passwd;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 角色类型
     */
    private Integer roleType;
    /**
     * 添加时间
     */
    private Date addDate;
    /**
     * 更新时间
     */
    private Date modifyDate;
    /**
     * 最后登录时间
     */
    private Date loginDate;
    /**
     * 是否删除: 0-未删除、1-已删除
     */
    private Integer deleted;

}
