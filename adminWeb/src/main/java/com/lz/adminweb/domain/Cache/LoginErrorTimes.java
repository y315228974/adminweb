package com.lz.adminweb.domain.Cache;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录次数
 *
 * @author pangshihe
 * @date 2020/9/23 11:22
 */
@Data
public class LoginErrorTimes implements Serializable {
    /**
     * 尝试登录时间
     */
    private long tryTimestamp;
    /**
     * 尝试登录次数
     */
    private int tryCount;

}
