package com.lz.adminweb.constant;

/**
 * Configs
 * @date 2020/6/11
 */
public class Configs {

    /**
     * 最大上传图片尺寸（M）
     */
    public static final int MAX_UPLOAD_SIZE = 20;

    /**
     * 缩略图
     */
    public static final String REPORT_PICTURE_THUMBNAIL = "90x90";

    /**
     * session缓存过期时间 单位/分钟
     */
    public static final int REDIS_SESSION_TIME_OUT = 30;

    /**
     * 验证码存储key
     */
    public static final String REDIS_KAPTCHA_TEMPTIME = "kaptcha_tempTime_";
}
