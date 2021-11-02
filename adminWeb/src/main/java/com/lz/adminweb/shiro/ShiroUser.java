package com.lz.adminweb.shiro;

import com.lz.adminweb.vo.system.MenuTreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Shiro用户信息
 *
 * @author comtu
 * @version 1.0
 * @date 2020/6/15  18:22
 */
@Data
public class ShiroUser implements Serializable {
    /**
     * token
     */
    @ApiModelProperty("token")
    private Serializable token;

    @ApiModelProperty("用户id")
    private long id;

    @ApiModelProperty(hidden = true)
    private LoginType loginType;

    /**
     * 性别：0-男、1-女
     */
    @ApiModelProperty("性别：0-男、1-女")
    private Integer sex;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String mobile;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String userName;

    /**
     * 角色类型
     */
    @ApiModelProperty("角色类型")
    private Integer roleType;

    @ApiModelProperty("用户菜单")
    List<MenuTreeNode> menuList;

}

