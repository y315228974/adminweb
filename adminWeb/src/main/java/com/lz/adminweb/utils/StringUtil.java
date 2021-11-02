package com.lz.adminweb.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * String工具类
 *
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/11
 */
public class StringUtil {

    private static Logger log = LoggerFactory.getLogger(StringUtil.class);

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return boolean
     * @author yaoyanhua
     * @date 2020/6/23 17:59
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 转long
     *
     * @param str 字符串
     * @return long
     * @author yaoyanhua
     * @date 2020/6/23 17:59
     */
    public static long stringToLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    /**
     * 转int
     *
     * @param str 字符串
     * @return int
     * @author yaoyanhua
     * @date 2020/6/23 18:00
     */
    public static int stringToInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    /**
     * 字符串转int数组
     *
     * @param str 字符串
     * @return java.util.List<java.lang.Integer>
     * @author yaoyanhua
     * @date 2020/6/23 18:00
     */
    public static List<Integer> stringToList(String str) {
        List<Integer> list = new ArrayList<>(0);
        if (StringUtils.isBlank(str)) {
            return list;
        }
        try {
            String[] strArr = str.split(",");
            for (String item : strArr) {
                list.add(Integer.parseInt(item));
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    /**
     * 用户身份证号码的打码隐藏加星号加*
     *
     * @return 处理完成的身份证
     */
    public static String stringMask(String val) {
        String res = "";
        if (!StringUtils.isEmpty(val)) {
            StringBuilder stringBuilder = new StringBuilder(val);
            if (val.length() >= 18) {
                res = stringBuilder.replace(4, 14, "**********").toString();
            } else if (val.length() >= 9) {
                res = stringBuilder.replace(2, 6, "****").toString();
            } else if (val.length() >= 3) {
                res = stringBuilder.replace(1, 2, "*").toString();
            } else if (val.length() >= 2) {
                res = stringBuilder.replace(1, 2, "*").toString();
            } else if (val.length() >= 1) {
                res = stringBuilder.replace(0, 1, "*").toString();
            }
        }
        return res;
    }

    /**
     * 转long
     *
     * @param str object对象
     * @return long
     * @author yaoyanhua
     * @date 2020/6/23 17:59
     */
    public static long objectToLong(Object str) {
        if (str == null) {
            return 0;
        }
        try {

            return Long.parseLong(str.toString());
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }
}
