package com.lz.adminweb.service.impl;

import com.lz.adminweb.domain.SystemPermission;
import com.lz.adminweb.domain.SystemRolePermission;
import com.lz.adminweb.domain.SystemUserRole;
import com.lz.adminweb.domain.UserPermission;
import com.lz.adminweb.mapper.PermissionMapper;
import com.lz.adminweb.mapper.RoleMapper;
import com.lz.adminweb.service.PermissionService;
import com.lz.adminweb.shiro.ShiroUser;
import com.lz.adminweb.shiro.ShiroUserUtil;
import com.lz.adminweb.utils.TreeUtil;
import com.lz.adminweb.vo.JsonResult;
import com.lz.adminweb.vo.system.PermissionTreeNode;
import com.lz.adminweb.vo.system.RolePermissionList;
import com.lz.adminweb.vo.system.RolePermissionTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by pangshihe on 2019/7/22.
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    PermissionMapper permissionMapper;
    @Autowired
    RoleMapper roleMapper;

    @Override
    public JsonResult savePermission(HttpServletRequest request, int permissionId, int permissionParentId, String permissionUrl,
                                     String permissionName, String permissionDescription, String permissionCode, int permissionType, String permissionIcon) {
        ShiroUser users = ShiroUserUtil.getUserInfo();
        if (users == null) {
            return JsonResult.fail("登录失效");
        }
        if (permissionId > 0) {
            SystemPermission editPermission = permissionMapper.selectPermissionDetail(permissionId);
            if (editPermission == null) {
                return JsonResult.fail("编辑失败，数据不存在");
            }
            editPermission.setPermissionUrl(permissionUrl);
            editPermission.setPermissionName(permissionName);
            editPermission.setPermissionDescription(permissionDescription);
            editPermission.setPermissionCode(permissionCode);
            editPermission.setPermissionType(permissionType);
            editPermission.setPermissionIcon(permissionIcon);
            int result = permissionMapper.updatePermission(editPermission);
            return result > 0 ? JsonResult.ok() : JsonResult.fail("编辑失败");
        } else {
            SystemPermission insertPermission = new SystemPermission();
            insertPermission.setPermissionUrl(permissionUrl);
            insertPermission.setPermissionName(permissionName);
            insertPermission.setPermissionDescription(permissionDescription);
            insertPermission.setPermissionCode(permissionCode);
            insertPermission.setPermissionType(permissionType);
            insertPermission.setPermissionIcon(permissionIcon);
            insertPermission.setPermissionParentId(permissionParentId);
            insertPermission.setCreateUser(users.getId());
            int insertResult = permissionMapper.insertPermission(insertPermission);
            return insertResult > 0 ? JsonResult.ok() : JsonResult.fail("新增失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult deletePermission(HttpServletRequest request, int permissionId) {
        //TODO 如果有子菜单或权限，全部删除
        List<SystemPermission> childPermission = permissionMapper.selectChildPermissionListByParentId(permissionId);
        if (childPermission != null && childPermission.size() > 0) {
            return JsonResult.fail("请先删除子节点");
        }
        int result = permissionMapper.deletePermission(permissionId);
        //角色对应的权限也删除
        permissionMapper.deleteRolePermissionByPermissionId(permissionId);
        return result > 0 ? JsonResult.ok() : JsonResult.fail("删除权限失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult editRolePermission(HttpServletRequest request, int roleId, String permissionIds) {
        ShiroUser users = ShiroUserUtil.getUserInfo();
        if (users == null) {
            return JsonResult.fail("登录失效");
        }
        if (StringUtils.isEmpty(permissionIds)) {
            return JsonResult.fail("请选择菜单或权限");
        }
        String[] permissions = permissionIds.split(",");
        if (permissions.length == 0) {
            return JsonResult.fail("权限id解析错误");
        }
        List<SystemRolePermission> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (!StringUtils.isEmpty(permission)) {
                SystemRolePermission rolePermission = new SystemRolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(Integer.parseInt(permission));
                rolePermission.setCreateUser(users.getId());
                permissionList.add(rolePermission);
            }
        }
        //删除角色所有权限
        permissionMapper.deleteRolePermission(roleId);
        int result = permissionMapper.addRolePermission(permissionList);
        return result > 0 ? JsonResult.ok() : JsonResult.fail("保存失败");
    }

    @Override
    public List<UserPermission> getUserPermissionList(int userId) {
        List<SystemUserRole> userRoleList = roleMapper.selectUserRoleList(userId);
        if (userRoleList == null || userRoleList.size() == 0) {
            return new ArrayList<>();
        }
        List<Integer> roleIds = userRoleList.stream().map(SystemUserRole::getRoleId).distinct().collect(Collectors.toList());
        List<UserPermission> userPermissionList = permissionMapper.selectPermissionListByRoleList(roleIds);
        if (userPermissionList.isEmpty()) {
            return new ArrayList<>();
        }
        //TODO 权限去重（保持原位置）
        Set<Integer> set = new HashSet<>();
        List<UserPermission> newList = new ArrayList<>();
        for (UserPermission element : userPermissionList) {
            if (element != null) {
                if (set.add(element.getPermissionId())) {
                    newList.add(element);
                }
            }
        }
        return newList;
    }

    /**
     * 获取角色权限树状结构
     *
     * @param roleId 角色id
     */
    @Override
    public JsonResult<List<RolePermissionTreeNode>> getRolePermissionList(HttpServletRequest request, int roleId) {
        //获取网站所有权限
        List<RolePermissionTreeNode> sitePermissionTreeNode = permissionMapper.getPermissionList();
        //获取角色的权限
        List<RolePermissionList> rolePermissionList = permissionMapper.getRolePermissionList(roleId);
        //数据拼接
        if (sitePermissionTreeNode != null && rolePermissionList != null && rolePermissionList.size() > 0) {
            for (RolePermissionTreeNode rolePermissionTreeNode : sitePermissionTreeNode) {
                for (RolePermissionList permissionList : rolePermissionList) {
                    if (rolePermissionTreeNode.getPermissionId() == permissionList.getPermissionId()) {
                        rolePermissionTreeNode.setHasPermission(true);
                        continue;
                    }
                }
            }
        }
        return JsonResult.ok(TreeUtil.rolePermissionToTree(sitePermissionTreeNode));
    }

    /**
     * 获取网站菜单和权限树状结构
     */
    @Override
    public JsonResult getPermissionListBySiteId() {
        List<PermissionTreeNode> rolePermissionListList = permissionMapper.selectMenuAndPermissionList();
        List<PermissionTreeNode> treeList = TreeUtil.listToTree(rolePermissionListList);
        return JsonResult.ok(treeList);
    }

    /**
     * 获取网站菜单树状结构
     *
     */
    @Override
    public JsonResult getMenuListBySiteId() {
        List<PermissionTreeNode> rolePermissionListList = permissionMapper.selectMenuList();
        List<PermissionTreeNode> treeList = TreeUtil.listToTree(rolePermissionListList);
        return JsonResult.ok(treeList);
    }

}
