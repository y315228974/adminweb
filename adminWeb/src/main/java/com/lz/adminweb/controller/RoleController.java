package com.lz.adminweb.controller;

import com.lz.adminweb.domain.SystemRole;
import com.lz.adminweb.service.RoleService;
import com.lz.adminweb.vo.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by pangshihe on 2019/7/19.
 */
@RestController
@RequestMapping("role")
@Api(value = "角色管理", tags = {"角色管理"})
public class RoleController {

    @Autowired
    RoleService roleService;

    @PostMapping(value = "saveRole")
    @ApiOperation(value = "编辑角色", notes = "编辑角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id，修改是必传", required = false, dataType = "int"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "roleDescription", value = "角色描述", required = true, dataType = "String"),
    })
    public JsonResult saveRole(HttpServletRequest request,
                               @RequestParam(value = "roleId", defaultValue = "0", required = false) Integer roleId,
                               @NotBlank(message = "角色名称不能为空") @Length(max = 50, message = "角色名称不能超过50个字符")
                               @RequestParam(value = "roleName" , defaultValue = "") String roleName,
                               @NotBlank(message = "角色描述不能为空") @Length(max = 500, message = "角色描述不能超过500个字符")
                               @RequestParam(value = "roleDescription" , defaultValue = "") String roleDescription) {
        return roleService.saveRole(request, roleId, roleName, roleDescription);
    }

    @PostMapping(value = "deleteRole")
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "int"),
    })
    public JsonResult deleteRole(HttpServletRequest request,
                                 @Min(value = 1, message = "角色id错误")
                                 @RequestParam(value = "roleId", defaultValue = "0") int roleId) {
        return roleService.deleteRole(request, roleId);
    }

    @PostMapping(value = "getRoleList")
    @ApiOperation(value = "获取所有角色", notes = "获取所有角色")
    public JsonResult<List<SystemRole>> getRoleList(HttpServletRequest request) {
        return roleService.getRoleList(request);
    }

    @PostMapping(value = "editUserRole")
    @ApiOperation(value = "编辑用户关联角色", notes = "编辑用户关联角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleIds", value = "角色id，多个逗号分隔", required = true, dataType = "int"),
    })
    public JsonResult editUserRole(HttpServletRequest request,
                                   @Min(value = 1, message = "用户错误")
                                   @RequestParam(value = "userId", defaultValue = "0") int userId,
                                   @RequestParam(value = "roleIds", defaultValue = "") String roleIds) {
        return roleService.editUserRole(request, userId, roleIds);
    }

    @PostMapping(value = "getUserRoleList")
    @ApiOperation(value = "获取用户关联角色", notes = "获取用户关联角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "int"),
    })
    public JsonResult getUserRoleList(HttpServletRequest request,
                                      @Min(value = 1, message = "用户错误")
                                      @RequestParam(value = "userId", defaultValue = "0") int userId) {
        return roleService.getUserRoleList(request, userId);
    }
}
