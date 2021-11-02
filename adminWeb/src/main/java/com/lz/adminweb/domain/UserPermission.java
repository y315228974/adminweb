package com.lz.adminweb.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by pangshihe on 2019/7/18.
 */
@Data
@ApiModel("用户权限")
public class UserPermission {

    @ApiModelProperty("权限id")
    private int permissionId;

    @ApiModelProperty("地址")
    private String permissionUrl;

    @ApiModelProperty("权限菜单名称")
    private String permissionName;
    /**
     * 权限码
     */
    @ApiModelProperty("权限码")
    private String permissionCode;
    /**
     * 权限类型 1 = 菜单 2 = 权限
     */
    @ApiModelProperty("权限类型 1 = 菜单 2 = 权限")
    private int permissionType;

}
