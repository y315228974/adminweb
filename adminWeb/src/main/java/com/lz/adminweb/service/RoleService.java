package com.lz.adminweb.service;

import com.lz.adminweb.vo.JsonResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by pangshihe on 2019/7/22.
 */
public interface RoleService {
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
    JsonResult saveRole(HttpServletRequest request, int roleId, String roleName, String roleDescription);

    JsonResult deleteRole(HttpServletRequest request, int roleId);

    JsonResult getRoleList(HttpServletRequest request);

    JsonResult editUserRole(HttpServletRequest request, int userId, String roleIds);

    JsonResult getUserRoleList(HttpServletRequest request, int userId);

    /**
     * 通过用户id获取角色id
     * @author: luzhichao
     * @param userId
     * @return: java.util.List<java.lang.Integer>
     * @date: 2021/8/23 15:15
     */
    List<Integer> getRoleIdsByUserId(int userId);
}
