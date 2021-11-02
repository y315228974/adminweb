package com.lz.adminweb.controller;

import com.lz.adminweb.service.PermissionService;
import com.lz.adminweb.vo.JsonResult;
import com.lz.adminweb.vo.system.PermissionTreeNode;
import com.lz.adminweb.vo.system.RolePermissionTreeNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
@RequestMapping("permission")
@Api(value = "权限菜单管理", tags = {"权限菜单管理"})
public class PermissionController {
    @Autowired
    PermissionService permissionService;

    @RequestMapping(value = "savePermission", method = RequestMethod.POST)
    @ApiOperation(value = "编辑权限菜单", notes = "编辑权限菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "permissionId", value = "权限主键id", required = false, dataType = "int"),
            @ApiImplicitParam(name = "permissionParentId", value = "父权限主键id", required = false, dataType = "int"),
            @ApiImplicitParam(name = "permissionUrl", value = "Url", required = false, dataType = "String"),
            @ApiImplicitParam(name = "permissionName", value = "权限名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "permissionDescription", value = "权限描述", required = true, dataType = "String"),
            @ApiImplicitParam(name = "permissionCode", value = "权限码", required = false, dataType = "String"),
            @ApiImplicitParam(name = "permissionType", value = "类型: 1 = 菜单 2 = 权限", required = true, dataType = "int"),
            @ApiImplicitParam(name = "permissionIcon", value = "图标", required = false, dataType = "String"),
    })
    public JsonResult savePermission(HttpServletRequest request,
                                     @RequestParam(value = "permissionId", defaultValue = "0", required = false) Integer permissionId,
                                     @Min(value = 0, message = "请选择父节点")
                                     @RequestParam(value = "permissionParentId", defaultValue = "0", required = false) Integer permissionParentId,
                                     @Length(max = 255, message = "Url不能超过255个字符")
                                     @RequestParam(value = "permissionUrl", defaultValue = "") String permissionUrl,
                                     @NotBlank(message = "权限名称不能为空") @Length(max = 50, message = "权限名称不能超过50个字符")
                                     @RequestParam(value = "permissionName", defaultValue = "") String permissionName,
                                     @NotBlank(message = "权限描述不能为空") @Length(max = 255, message = "权限描述不能超过255个字符")
                                     @RequestParam(value = "permissionDescription", defaultValue = "") String permissionDescription,
                                     @Length(max = 255, message = "权限码不能超过255个字符")
                                     @RequestParam(value = "permissionCode", defaultValue = "", required = false) String permissionCode,
                                     @Min(value = 1, message = "类型错误")
                                     @RequestParam(value = "permissionType", defaultValue = "0") int permissionType,
                                     @Length(max = 255, message = "图标不能超过255个字符")
                                     @RequestParam(value = "permissionIcon", defaultValue = "", required = false) String permissionIcon) {

        return permissionService.savePermission(request, permissionId, permissionParentId, permissionUrl,
                permissionName, permissionDescription, permissionCode, permissionType, permissionIcon);
    }

    @RequestMapping(value = "deletePermission", method = RequestMethod.POST)
    @ApiOperation(value = "删除权限菜单", notes = "删除权限菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "permissionId", value = "权限主键id", required = true, dataType = "int"),
    })
    public JsonResult deletePermission(HttpServletRequest request,
                                       @Min(value = 1, message = "权限错误") @RequestParam(value = "permissionId", defaultValue = "0") int permissionId) {

        return permissionService.deletePermission(request, permissionId);
    }

    @RequestMapping(value = "editRolePermission", method = RequestMethod.POST)
    @ApiOperation(value = "编辑角色关联权限", notes = "编辑角色关联权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "permissionIds", value = "权限主键id,多个逗号分隔", required = true, dataType = "String"),
    })
    public JsonResult editRolePermission(HttpServletRequest request,
                                         @Min(value = 1, message = "角色错误")
                                         @RequestParam(value = "roleId", defaultValue = "0") int roleId,
                                         @NotBlank(message = "请选择菜单或权限")
                                         @RequestParam(value = "permissionIds", defaultValue = "") String permissionIds) {
        return permissionService.editRolePermission(request, roleId, permissionIds);
    }

    @RequestMapping(value = "getRolePermissionList", method = RequestMethod.POST)
    @ApiOperation(value = "获取角色关联权限", notes = "获取角色关联权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "int"),
    })
    public JsonResult<List<RolePermissionTreeNode>> getRolePermissionList(HttpServletRequest request,
                                                                          @Min(value = 1, message = "角色错误") @RequestParam(value = "roleId", defaultValue = "0") int roleId) {
        return permissionService.getRolePermissionList(request, roleId);
    }

    //@RequestMapping(value = "getPermissionList", method = RequestMethod.POST)
    //@ApiOperation(value = "获取网站菜单和权限树状结构", notes = "获取网站菜单和权限树状结构")
    //public JsonResult<List<PermissionTreeNode>> getPermissionList() {
    //    return permissionService.getPermissionList();
    //}

    @RequestMapping(value = "getMenuList", method = RequestMethod.POST)
    @ApiOperation(value = "获取网站菜单树状结构", notes = "获取网站菜单树状结构")
    public JsonResult<List<PermissionTreeNode>> getMenuList() {
        return permissionService.getMenuList();
    }

}
