package com.lz.adminweb.service.impl;

import com.lz.adminweb.domain.SystemRole;
import com.lz.adminweb.domain.SystemUserRole;
import com.lz.adminweb.enums.YesOrNoEnum;
import com.lz.adminweb.mapper.PermissionMapper;
import com.lz.adminweb.mapper.RoleMapper;
import com.lz.adminweb.service.RoleService;
import com.lz.adminweb.shiro.ShiroUser;
import com.lz.adminweb.shiro.ShiroUserUtil;
import com.lz.adminweb.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleMapper roleMapper;
    @Autowired
    PermissionMapper permissionMapper;

    /**
     * 保存角色
     * @author: luzhichao
     * @param request
     * @param roleId
     * @param roleName
     * @param roleDescription
     * @return: com.lz.adminweb.vo.JsonResult
     * @date: 2021/11/2 10:26
     */
    @Override
    public JsonResult saveRole(HttpServletRequest request, int roleId, String roleName, String roleDescription) {
        ShiroUser users = ShiroUserUtil.getUserInfo();
        if (users == null) {
            return JsonResult.fail("登录失效");
        }
        if (roleId > 0) {
            SystemRole updateRole = roleMapper.selectRoleDetail(roleId);
            if (updateRole == null) {
                return JsonResult.fail("编辑失败，数据不存在");
            }
            updateRole.setRoleName(roleName);
            updateRole.setRoleDescription(roleDescription);
            int updateResult = roleMapper.updateRole(updateRole);
            return updateResult > 0 ? JsonResult.ok() : JsonResult.fail("更新失败");
        } else {
            SystemRole insertRole = new SystemRole();
            insertRole.setRoleName(roleName);
            insertRole.setRoleDescription(roleDescription);
            insertRole.setIsActive(YesOrNoEnum.YES.getValue());
            insertRole.setCreateUser(users.getId());
//            insertRole.setCreateDate(new Date());
            int insertResult = roleMapper.insertRole(insertRole);
            return insertResult > 0 ? JsonResult.ok() : JsonResult.fail("新增失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult deleteRole(HttpServletRequest request, int roleId) {
        int result = roleMapper.deleteRole(roleId);
        //删除角色对应的权限
        permissionMapper.deleteRolePermission(roleId);
        return result > 0 ? JsonResult.ok() : JsonResult.fail("删除失败");
    }

    /**
     * 获取所有角色
     * @author: luzhichao
     * @param request
     * @return: com.lz.adminweb.vo.JsonResult
     * @date: 2021/11/2 10:34
     */
    @Override
    public JsonResult getRoleList(HttpServletRequest request) {
        List<SystemRole> roleList = roleMapper.selectRoleList();
        return JsonResult.ok(roleList);
    }

    /**
     * 编辑用户关联角色
     * @author: luzhichao
     * @param request
     * @param userId
     * @param roleIds
     * @return: com.lz.adminweb.vo.JsonResult
     * @date: 2021/11/2 10:51
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult editUserRole(HttpServletRequest request, int userId, String roleIds) {
        int result = roleMapper.deleteUserRole(userId);
        if (!StringUtils.isEmpty(roleIds)) {
            String[] roleId = roleIds.split(",");
            List<SystemUserRole> userRole = new ArrayList<>();
            for (String s : roleId) {
                if (!StringUtils.isEmpty(s)) {
                    SystemUserRole systemUserRole = new SystemUserRole();
                    systemUserRole.setUserId(userId);
                    systemUserRole.setRoleId(Integer.parseInt(s));
                    userRole.add(systemUserRole);
                }
            }
            result = roleMapper.addUserRoleBitch(userRole);
        }
        return result > 0 ? JsonResult.ok() : JsonResult.fail("添加失败");
    }

    /**
     * 获取用户角色
     * @author: luzhichao
     * @param request
     * @param userId
     * @return: com.lz.adminweb.vo.JsonResult
     * @date: 2021/11/2 10:36
     */
    @Override
    public JsonResult getUserRoleList(HttpServletRequest request, int userId) {
        List<SystemRole> roleList = roleMapper.getUserRoleList(userId);
        return JsonResult.ok(roleList);
    }

    /**
     * 通过用户id获取角色id
     * @author: luzhichao
     * @param userId
     * @return: java.util.List<java.lang.Integer>
     * @date: 2021/8/23 15:15
     */
    @Override
    public List<Integer> getRoleIdsByUserId(int userId) {
        List<SystemUserRole> userRoleList = roleMapper.selectUserRoleList(userId);
        List<Integer> collect = userRoleList.stream().map(SystemUserRole::getRoleId).distinct().collect(Collectors.toList());
        return collect;
    }
}
