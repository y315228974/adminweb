package com.lz.adminweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lz.adminweb.domain.SystemRole;
import com.lz.adminweb.domain.SystemUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 */
@Mapper
public interface RoleMapper extends BaseMapper<SystemRole> {

    int insertRole(SystemRole systemRole);

    int updateRole(SystemRole systemRole);

    /**
     * 逻辑删除角色
     *
     * @param roleId 角色id
     */
    int deleteRole(@Param("roleId") int roleId);

    List<SystemRole> selectRoleList();

    SystemRole selectRoleDetail(@Param("roleId") int roleId);

    int addUserRoleBitch(@Param("userRoleList") List<SystemUserRole> userRoleList);

    /**
     * 删除用户网站所有角色
     *
     * @param userId 用户id
     */
    int deleteUserRole(@Param("userId") int userId);

    /**
     * 查询用户角色
     */
    List<SystemRole> getUserRoleList(@Param("userId") int userId);

    /**
     * 查询用户角色(不包含角色名称等字段)
     */
    List<SystemUserRole> selectUserRoleList(@Param("userId") int userId);
}
