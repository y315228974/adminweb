package com.lz.adminweb.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 */
@Data
@TableName("system_role")
public class SystemRole {
    private int roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色描述
     */
    private String roleDescription;
    private long createUser;
    private Date createDate;
    /**
     * 角色状态 0 = 失效 ， 1 = 有效
     */
    private int isActive;
    /**
     * 是否拥有权限
     */
    private boolean isHasRole;

}