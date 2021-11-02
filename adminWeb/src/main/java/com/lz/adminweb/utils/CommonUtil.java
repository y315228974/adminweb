package com.lz.adminweb.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 公共工具类
 *
 * @author pangshihe
 * @date 2020/6/15 10:16
 */
@Slf4j
public class CommonUtil {

    /**
     * 判断是不是Windows系统.
     *
     * @return 返回是不是Windows系统.
     */
    public static boolean isOsWindows() {
        String osname = System.getProperty("os.name").toLowerCase();
        boolean rt = osname.startsWith("windows");
        return rt;
    }

    /**
     * 将路径修正为当前操作系统所支持的形式.
     *
     * @param path 源路径.
     * @return 返回修正后的路径.
     */
    public static String fixPath(String path) {
        if (null == path) return path;
        if (path.length() >= 1 && ('/' == path.charAt(0) || '\\' == path.charAt(0))) {
            // 根目录, Windows下需补上盘符.
            if (isOsWindows()) {
                String userdir = System.getProperty("user.dir");
                if (null != userdir && userdir.length() >= 2) {
                    return userdir.substring(0, 2) + path;
                }
            }
        }
        return path;
    }

    /**
     * 字符串返回处理
     *
     * @return
     */
    public static String getStringSymbolProcessing(String s) {
        s = s.replaceAll("\'", "&prime;").replaceAll("<", "&#60;").replaceAll(">", "&#62;").replaceAll("\"", "&quot;").replaceAll("\n", "<br>\n");
        return s;
    }


    /**
     * 把特殊字符全替换成空格
     *
     * @param character
     * @return
     */
    public static String getSpecialCharacter(String character) {
        String regEx = "[`~!@#$%^&*()+=|{}<>'\";',?-]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(character);
        return m.replaceAll(" ").trim();
    }

    /**
     * 转换文件大小
     */
    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * @param path   原图路径
     * @param width  生成的宽度
     * @param height 高度
     * @return java.lang.String
     * @Description:获取缩略图路径
     * @author liuzhaowei
     * @date 2020/7/27
     */
    public static String getThumbPath(String path, int width, int height) {
        if (StringUtils.isBlank(path)) {
            return "";
        }
        String[] arr = path.split("\\.");
        String thumbPath = String.format("%s_%sx%s.%s", arr[0], width, height, arr[1]);
        return thumbPath;
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param phone 移动、联通、电信运营商的号码段
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isPhone(String phone) {
        String regex = "^(\\+\\d+)?1[123456789]\\d{9}$";
        return Pattern.matches(regex, phone);
    }


    /**
     * 验证数字
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkNumber(String digit) {
        String regex = "^[0-9]+$";
        return Pattern.matches(regex, digit);
    }

    /**
     * 验证香港手机号
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkHKPhone(String digit) {
        String regex = "^[0-9]{1,10}$";
        return Pattern.matches(regex, digit);
    }

    /**
     * 验证居民省份证
     *
     * @param
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String value) {
        String regex = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
        return Pattern.matches(regex, value);
    }

    /**
     * 验证港澳居民来往内地通行证
     *
     * @param
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkHomecomingPermit(String value) {
        //String regex = "^((\\s?[A-Za-z])|([A-Za-z]{2}))\\d{6}((\\([0-9aA]\\))|([0-9aA]))$";
        String regex = "^[HMhm](\\d{8}|\\d{10})$";
        return Pattern.matches(regex, value);
    }

    /**
     * 验证往来港澳通行证
     *
     * @param
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIDHK(String value) {
        String regex = "^[a-zA-Z0-9]{6,10}$";
        return Pattern.matches(regex, value);
    }


    /**
     * 验证台湾居民来往内地通行证
     * 2013年前是1个英文字+7位数字，于2013年改为8位数字。
     *
     * @param
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIDTaiwan(String value) {
        String regex = "^[a-zA-Z]?[0-9]{7,8}$";
        return Pattern.matches(regex, value);
    }

    /**
     * 护照验证
     * 规则： G + 8位数字, P + 7位数字, S/D + 7或8位数字,等
     * 例： G12345678, P1234567
     */
    public static Boolean checkPassportCard(String card) {
        String reg = "^([a-zA-Z]|[0-9]){5,17}$";
        if (card.matches(reg) == false) {
            //护照号码不合格
            return  false;
        } else {
            //校验通过
            return true;
        }
    }

    // 百分比计算
    private final static NumberFormat nf = NumberFormat.getPercentInstance();
    static {
        nf.setMinimumFractionDigits(2); // 设置两位小数位
    }
    /**
     * @description: 计算百分比 保留两位小时
     * @param valueNew 当前数值
     * @param valueOld 对比数值
     * @return: void
     * @author: luzhichao
     * @date: 2021/2/24 14:57
     */
    public static String calculatedPercentage(double valueNew, double valueOld) {
        String result;
        if (valueNew == valueOld) {
            result = "0.00%";
        } else if (valueOld == 0.0) {
            result = "100.00%";
        } else if (valueNew == 0.0) {
            result = "-100.00%";
        } else {
            double resultValue = (valueNew - valueOld) / valueOld;
            result = nf.format(resultValue);
        }
        return result;
    }


}
