package com.lz.adminweb.utils;

import lombok.extern.log4j.Log4j2;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 创建人
 * LsYearApiSystem
 * 2021/1/22 19:04
 * 文件导出工具
 *
 * @author luzhichao
 **/
@Log4j2
public class FileExportUtil {

    /**
     * 返回头名称设置
     */
    public static String getHeaderValue(String fileName) {
        String headerValue = "attachment;";
        headerValue += " filename=\"" + encodeURIComponent(fileName) + "\";";
        headerValue += " filename*=utf-8''" + encodeURIComponent(fileName);
        return headerValue;
    }

    private static String encodeURIComponent(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            log.error("encodeURIComponent失败：", e);
            return null;
        }
    }
}
