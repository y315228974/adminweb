package com.lz.adminweb.domain;

import lombok.Data;

import java.util.Date;

/**
 */
@Data
public class SystemRolePermission {
    private int roleId;
    private int permissionId;
    private long createUser;
    private Date createDate;
}
