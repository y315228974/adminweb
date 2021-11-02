package com.lz.adminweb.utils;

import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;

import com.alibaba.druid.util.StringUtils;

/**
 * @className ApachePoiUtil
 * @Description apachePOI Excel样式库
 * @Author 11
 * @Date 2021/1/23 16:05
 **/
public class ApachePoiUtil {
    public static void setBorderStyle(Workbook wb, CellStyle cellStyle,Boolean isTitle){
        if(isTitle){
            XSSFFont font = (XSSFFont) wb.createFont();
            font.setFontHeightInPoints((short) 11);
            font.setFontName("微软雅黑");
            font.setBold(true);
            cellStyle.setFont(font);
        }
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        // 水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    /*** 标题加粗
     * @param workBook
     * @param fontSize
     * @return org.apache.poi.ss.usermodel.Font
     * @author clj
     * @date 2021/1/23 16:11
     */
    public static Font setFontStyle(Workbook workBook, short fontSize) {
        Font font = workBook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints(fontSize);// 设置字体大小
        font.setBold(true);// 粗体显示
        return font;
    }
    
    /**
     * 	处理数字字符串sheet(poi默认数字是double型)
     * @param value
     * @return_type String
     * @author 蔡李佳
     * @date 2021-01-25 02:30:25
     */
    public static String numberDataUtil(String value) {
        Pattern pattern = Pattern.compile("\\d+||\\d*\\.\\d+||\\d*\\.?\\d+?e[+-]\\d*\\.?\\d+?||e[+-]\\d*\\.?\\d+?");
    	 if(!StringUtils.isEmpty(value)) {
    		 if(pattern.matcher(value).matches() && value.lastIndexOf(".")!=-1) {
    			 return value.substring(0,value.lastIndexOf("."));
    		 }
    	 }
    	 return value;
    }
}
