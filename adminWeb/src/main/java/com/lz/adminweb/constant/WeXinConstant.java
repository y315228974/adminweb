//package com.lz.adminweb.constant;
//
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
///**
// * @Auther:luzhichao
// * @Date:2021/11/3 15:15
// * @Description: 微信配置常量
// */
//@Log4j2
//@Component
//public class WeXinConstant {
//
//    /** 公众账号ID */
//    public static String appID;
//
//    /** 商户号 */
//    public static String mchID;
//
//    /** 密钥key(参数加密公众号配置) */
//    public static String key;
//
//    /** 安全证书路径 */
//    public static String certPath;
//
//
//
//    // -------------------------------------------------------------------
//    @PostConstruct
//    public void init() {
//        appID = _appID;
//        mchID = _mchID;
//        key = _key;
//        certPath = _certPath;
//    }
//
//    @Value("${WXPAY.appID}")
//    private String _appID;
//    @Value("${WXPAY.mchID}")
//    private String _mchID;
//    @Value("${WXPAY.key}")
//    private String _key;
//    @Value("${WXPAY.certPath}")
//    private String _certPath;
//
//}
