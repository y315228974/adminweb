package com.lz.adminweb.config;

import com.lz.adminweb.utils.wxpay.IWXPayDomain;
import com.lz.adminweb.utils.wxpay.WXPayConfig;
import com.lz.adminweb.utils.wxpay.WXPayConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @Auther:luzhichao
 * @Date:2021/11/3 15:09
 * @Description:
 */
@Configuration
public class MyWxPayConfig extends WXPayConfig {

    @Value("${WXPAY.appID}")
    private String appID;
    @Value("${WXPAY.mchID}")
    private String mchID;
    @Value("${WXPAY.key}")
    private String key;
    @Value("${WXPAY.certPath}")
    private String certPath;

    private byte[] certData;

    @PostConstruct
    public void init() throws Exception {
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public String getAppID() {
        return appID;
    }

    public String getMchID() {
        return mchID;
    }

    public String getKey() {
        return key;
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {
                // 上报网络信息

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                DomainInfo domainInfo = new DomainInfo(WXPayConstants.DOMAIN_API, true);
                return domainInfo;
            }
        };
        return iwxPayDomain;
    }
}
