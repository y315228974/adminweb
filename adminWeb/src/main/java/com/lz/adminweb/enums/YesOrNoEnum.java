package com.lz.adminweb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesOrNoEnum {

    NO(0, "否"),

    YES(1, "是");

    private final int value;
    private final String name;

    public static YesOrNoEnum fromValue(Integer value) {
        if (value != null) {
            for (YesOrNoEnum yesOrNoEnum : YesOrNoEnum.values()) {
                if (yesOrNoEnum.getValue() == value) {
                    return yesOrNoEnum;
                }
            }
        }
        return null;
    }

}
