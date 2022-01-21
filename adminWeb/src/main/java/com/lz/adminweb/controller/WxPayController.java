package com.lz.adminweb.controller;

import com.lz.adminweb.config.MyWxPayConfig;
import com.lz.adminweb.utils.wxpay.WXPay;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther:luzhichao
 * @Date:2021/11/3 15:30
 * @Description:
 */
@RestController
@RequestMapping(value = "wxPay")
@Api(value = "微信支付模块", tags = {"微信支付模块"})
public class WxPayController {

    @Resource
    MyWxPayConfig myWxPayConfig;

    @GetMapping("test")
    public void test() throws Exception {
        WXPay wxPay = new WXPay(myWxPayConfig);
        Map<String, String> data = new HashMap<String, String>();
        data.put("body", "腾讯充值中心-QQ会员充值");
        data.put("out_trade_no", "2016090910595900000012");
        data.put("device_info", "");
        data.put("fee_type", "CNY");
        data.put("total_fee", "1");
        data.put("spbill_create_ip", "123.12.12.123");
        data.put("notify_url", "http://www.example.com/wxpay/notify");
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
        data.put("product_id", "12");
        try {
            Map<String, String> resp = wxPay.unifiedOrder(data);
            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
