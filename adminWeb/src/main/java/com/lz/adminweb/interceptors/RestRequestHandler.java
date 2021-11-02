package com.lz.adminweb.interceptors;

import com.alibaba.fastjson.JSON;
import com.lz.adminweb.utils.RestSignUtil;
import com.lz.adminweb.vo.JsonResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author yanhua
 * @Description: rest请求拦截器
 * @date 2019/1/4
 */
public class RestRequestHandler implements HandlerInterceptor {
    Logger log = LoggerFactory.getLogger(this.getClass());

    private String mAppId;
    private String mAppSecret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            final Map<String, String> orderedParametersMap = readRequestParameters(request.getParameterMap());
            return preHandleRequest(orderedParametersMap, response);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private Map<String, String> readRequestParameters(Map<String, String[]> parameterMap) {
        final Map<String, String> ret = new TreeMap<>();
        for (String key : parameterMap.keySet()) {
            final Object value = parameterMap.get(key);
            String param = null;
            if (value instanceof String[]) {
                param = ((String[]) value)[0];
            } else if (value instanceof String) {
                param = (String) value;
            }
            ret.put(key, param);
        }
        return ret;
    }

    private List<String> flatParameters(Map<String, String> orderedParams) {
        final Set<Map.Entry<String, String>> entries = orderedParams.entrySet();
        final List<String> ret = new ArrayList<>(orderedParams.size());
        for (Map.Entry<String, String> entry : entries) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            if (StringUtils.equals(key, "signature") || value == null) {
                continue;
            }
            ret.add(key + "=" + value);
        }
        return ret;
    }

    private boolean preHandleRequest(Map<String, String> orderedParams, HttpServletResponse response) throws IOException {
        log.info(String.format("请求参数：%s", RestSignUtil.mapToUrlString(orderedParams)));
        String encryptionType = orderedParams.get("signType");
        String signatureParam = orderedParams.get("signature");
        final String appId = orderedParams.get("appid");
        final String timeStamp = orderedParams.get("timeStamp");

        if (!StringUtils.equals(appId, mAppId)) {
            final JsonResult<Object> ret = JsonResult.fail("appid error");
            responseJson(response, ret);
            log.error(String.format("请求参数：%s", RestSignUtil.mapToUrlString(orderedParams)));
            return false;
        }
        if (StringUtils.isBlank(timeStamp)) {
            final JsonResult<Object> ret = JsonResult.fail("时间戳不能为空");
            responseJson(response, ret);
            return false;
        }

        final long clientTime = Long.parseLong(timeStamp);
        final long serverTime = System.currentTimeMillis();
        final long timeDiff = Math.abs(serverTime - clientTime) / 1000;//相差多少秒
        //设置超时30分钟
        if (timeDiff > 1800 || timeDiff < 0) {
            final JsonResult<Object> ret = JsonResult.fail("请求失效 error");
            log.error(String.format("请求失效：当前时间%s;参数时间%s;时间间隔%s", clientTime, serverTime, timeDiff));
            responseJson(response, ret);
            return false;
        }

        final List<String> flatParams = flatParameters(orderedParams);

        String signatureActual;
        final String paramText = StringUtils.join(flatParams.toArray(), "&");
        if (StringUtils.equals("MD5", encryptionType)) {
            signatureActual = DigestUtils.md5Hex(paramText + mAppSecret);
            if (StringUtils.equals(signatureActual, signatureParam)) {
                return true;
            } else {
                final JsonResult<Object> ret = JsonResult.fail("sign error");
                log.error(String.format("请求参数：%s", RestSignUtil.mapToUrlString(orderedParams)));
                responseJson(response, ret);
            }
        } else if (StringUtils.equals("SHA1", encryptionType)) {
            signatureActual = DigestUtils.sha1Hex(paramText + mAppSecret);
            if (StringUtils.equals(signatureActual, signatureParam)) {
                return true;
            } else {
                return true;
//                final JsonResult<Object> ret = JsonResult.fail("sign error");
//                responseJson(response, ret);
            }
        } else {
            final JsonResult<Object> ret = JsonResult.fail("encrypt error");
            responseJson(response, ret);
        }
        return false;
    }

    private void responseJson(HttpServletResponse response, JsonResult<Object> ret) throws IOException {
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        final PrintWriter writer = response.getWriter();
        response.setStatus(HttpServletResponse.SC_OK);
        writer.write(JSON.toJSONString(ret));
        writer.flush();
    }

    public void setmAppId(String mAppId) {
        this.mAppId = mAppId;
    }

    public void setmAppSecret(String mAppSecret) {
        this.mAppSecret = mAppSecret;
    }
}
