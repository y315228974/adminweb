package com.lz.adminweb.shiro;

import com.alibaba.fastjson.JSONObject;
import com.lz.adminweb.vo.JsonResult;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.PrintWriter;

/**
 * 登出过滤器
 *
 * @author yaoyanhua
 * @version 1.0
 * @date 2021/1/23
 */
public class CustomLogoutFilter extends LogoutFilter {
    private static final Logger log = LoggerFactory.getLogger(CustomLogoutFilter.class);

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        try {
            subject.logout();
        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        }
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(JSONObject.toJSONString(JsonResult.ok()));
        out.flush();
        out.close();
        return false;
    }

}
