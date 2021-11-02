package com.lz.adminweb.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色权限列表
 * Created by pangshihe on 2019/7/18.
 */
@ApiModel("角色关联权限")
@Data
public class RolePermissionList {

    @ApiModelProperty("权限id")
    private int permissionId;
    @ApiModelProperty("角色id")
    private int roleId;
}
