package com.lz.adminweb.domain;

import lombok.Data;

/**
 * Created by pangshihe on 2019/7/18.
 */
@Data
public class UserPermission {

    private int permissionId;
    private String permissionUrl;
    private String permissionName;
    /**
     * 权限码
     */
    private String permissionCode;
    /**
     * 权限类型 1 = 菜单 2 = 权限
     */
    private int permissionType;

}
