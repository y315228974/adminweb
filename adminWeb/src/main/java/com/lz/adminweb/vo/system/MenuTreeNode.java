package com.lz.adminweb.vo.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
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
     * 菜单名称
     */
    private String title;
    /**
     * url
     */
    private String name;

    private List<MenuTreeNode> children = new ArrayList<>();

}