package com.lz.adminweb.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("权限菜单树")
public class PermissionTreeNode {

    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("父id")
    private int parentId;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("地址url")
    private String url;
    @ApiModelProperty("描述")
    private String description;
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
    /**
     * 图标
     */
    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("子菜单")
    private List<PermissionTreeNode> children = new ArrayList<>();

}