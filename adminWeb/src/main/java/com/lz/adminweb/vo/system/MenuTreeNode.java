package com.lz.adminweb.vo.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("菜单树")
public class MenuTreeNode {
    @JsonIgnore
    private int id;
    @JsonIgnore
    private int parentId;
    /**
     * url
     */
    @ApiModelProperty("url")
    private String url;
    /**
     * 菜单名称
     */
    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("图标")
    private String permissionIcon;

    @ApiModelProperty("子菜单")
    private List<MenuTreeNode> children = new ArrayList<>();

}