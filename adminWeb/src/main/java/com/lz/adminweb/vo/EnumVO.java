package com.lz.adminweb.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author jxc
 * @date 2021/1/28
 */
@Data
public class EnumVO {
    public EnumVO() {
    }

    public EnumVO(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @ApiModelProperty(value = "枚举编号")
    private int code;

    @ApiModelProperty(value = "枚举名称")
    private String value;
}
