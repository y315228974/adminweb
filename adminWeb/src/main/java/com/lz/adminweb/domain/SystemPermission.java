package com.lz.adminweb.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Created by pangshihe on 2019/7/18.
 */
@Data
@TableName("system_permission")
public class SystemPermission {
    private int permissionId;
    /**
     * 父权限id
     */
    private int permissionParentId;
    private String permissionUrl;
    private String permissionName;
    private String permissionDescription;
    private long createUser;
    private Date createDate;
    /**
     * 权限码
     */
    private String permissionCode;
    /**
     * 权限类型 1 = 菜单 2 = 权限
     */
    private int permissionType;
    /**
     * 图标
     */
    private String permissionIcon;

}
