package com.lz.adminweb.service;

import com.lz.adminweb.domain.UserPermission;
import com.lz.adminweb.vo.JsonResult;
import com.lz.adminweb.vo.system.MenuTreeNode;
import com.lz.adminweb.vo.system.RolePermissionTreeNode;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by pangshihe on 2019/7/22.
 */
public interface PermissionService {
    JsonResult savePermission(HttpServletRequest request, int permissionId, int permissionParentId, String permissionUrl, String permissionName,
                              String permissionDescription, String permissionCode, int permissionType, String permissionIcon);

    JsonResult deletePermission(HttpServletRequest request, int permissionId);

    /**
     * 编辑角色权限
     *
     * @param roleId        角色id
     * @param permissionIds 权限id集合，字符串英文,分割
     * @return
     */
    JsonResult editRolePermission(HttpServletRequest request, int roleId, String permissionIds);

    /**
     * 获取用户网站权限列表
     *
     * @param userId 用户id
     */
    List<UserPermission> getUserPermissionList(int userId);

    /**
     * 获取角色权限树状结构
     *
     * @param roleId 角色id
     */
    JsonResult<List<RolePermissionTreeNode>> getRolePermissionList(HttpServletRequest request, int roleId);

    /**
     * 获取网站菜单和权限树状结构
     *
     */
    JsonResult getPermissionList();

    /**
     * 获取网站菜单树状结构
     *
     */
    JsonResult getMenuList();

    /**
     * 获取用户网站菜单
     *
     * @param userId   用户id
     */
    List<MenuTreeNode> getUserMenuList(long userId);

}
