package com.lz.adminweb.vo.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色权限树状列表
 */
@Data
@ApiModel("角色权限树状列表")
public class RolePermissionTreeNode {
    @ApiModelProperty("权限id")
    private int permissionId;
    /**
     * 父权限id
     */
    @ApiModelProperty("父权限id")
    private int permissionParentId;
    @JsonIgnore
    private String permissionUrl;
    @ApiModelProperty("权限名称")
    private String permissionName;
    @JsonIgnore
    private String permissionDescription;
    @JsonIgnore
    private int siteId;
    @JsonIgnore
    private int createUser;
    @JsonIgnore
    private Date createDate;
    /**
     * 权限码
     */
    @JsonIgnore
    private String permissionCode;
    /**
     * 权限类型 1 = 菜单 2 = 权限
     */
    @ApiModelProperty("权限类型 1 = 菜单 2 = 权限")
    private int permissionType;
    /**
     * 图标
     */
    @JsonIgnore
    private String permissionIcon;
    /**
     * 是否拥有该权限
     */
    @ApiModelProperty("是否拥有该权限")
    private boolean isHasPermission;

    private List<RolePermissionTreeNode> children = new ArrayList<>();

}
