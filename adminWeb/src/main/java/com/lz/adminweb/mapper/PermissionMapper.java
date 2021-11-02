package com.lz.adminweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lz.adminweb.domain.SystemPermission;
import com.lz.adminweb.domain.SystemRolePermission;
import com.lz.adminweb.domain.UserPermission;
import com.lz.adminweb.vo.system.MenuTreeNode;
import com.lz.adminweb.vo.system.PermissionTreeNode;
import com.lz.adminweb.vo.system.RolePermissionList;
import com.lz.adminweb.vo.system.RolePermissionTreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 */
@Mapper
public interface PermissionMapper extends BaseMapper<SystemPermission> {

    int insertPermission(SystemPermission permission);

    int updatePermission(SystemPermission permission);

    int deletePermission(@Param("permissionId") int permissionId);

    SystemPermission selectPermissionDetail(@Param("permissionId") int permissionId);

    /**
     * 批量添加权限
     */
    int addRolePermission(@Param("rolePermissionList") List<SystemRolePermission> rolePermissionList);

    /**
     * 删除角色所有权限
     *
     * @param roleId 角色id
     */
    int deleteRolePermission(@Param("roleId") int roleId);

    /**
     * 查询角色权限
     *
     * @param roleList 角色列表
     */
    List<UserPermission> selectPermissionListByRoleList(@Param("roleList") List<Integer> roleList);

    /**
     * 删除角色权限(根据权限id)
     *
     * @param permissionId 权限id
     */
    int deleteRolePermissionByPermissionId(@Param("permissionId") int permissionId);

    /**
     * 获取子权限或菜单
     *
     * @param permissionId 菜单、权限id
     */
    List<SystemPermission> selectChildPermissionListByParentId(@Param("permissionId") int permissionId);

    /**
     * 获取角色权限
     *
     * @param roleId 角色id
     */
    List<RolePermissionList> getRolePermissionList(@Param("roleId") int roleId);

    /**
     * 获取网站权限
     */
    List<RolePermissionTreeNode> getPermissionList();

    /**
     * 获取网站菜单
     *
     */
    List<PermissionTreeNode> selectMenuList();

    /**
     * 获取网站菜单和权限
     *
     */
    List<PermissionTreeNode> selectMenuAndPermissionList();

    /**
     * 获取用户菜单
     */
    List<MenuTreeNode> selectUserMenuListByRoleList(@Param("roleList") List<Integer> roleList);

}
