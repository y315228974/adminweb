package com.lz.adminweb.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;

/**
 * MD5加盐工具类
 *
 * @author pangshihe
 * @date 2020/7/6 9:35
 */
public class MD5Util {

    /**
     * 生成16位随机数盐
     */
    public static String generateSalt() {
        SecureRandom r = new SecureRandom();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        return sb.toString();
    }

    /**
     * MD5再加盐 MD5
     */
    public static String md5WithSalt(String password) {
        String salt = "cB5SlfvZFL29b7UtLzQP2TnOqrOJvC1TCI7ZuD26h3vRiRN9RAUBiLs1Co4PA8Kl";
        if (!StringUtils.isEmpty(password) && !StringUtils.isEmpty(salt)) {
            return md5(password + salt);
        } else {
            return "";
        }
    }

    public static void main(String[] args) {
        System.out.println(md5WithSalt("123456"));
    }

    private static String md5(String password) {
        if (!StringUtils.isEmpty(password)) {
            return DigestUtils.md5Hex(password);
        } else {
            return "";
        }
    }
}
