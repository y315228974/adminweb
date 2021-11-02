package com.lz.adminweb.exception;

import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.fastjson.JSONObject;
import com.lz.adminweb.enums.ResponseCodeEnum;
import com.lz.adminweb.utils.HttpUtil;
import com.lz.adminweb.vo.JsonResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 全局异常类
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/11
 */
@ControllerAdvice
public class GlobalException {
    private static final Pattern FILTER_EXCEPTION1_PATTERN = Pattern.compile("request method \'get\' not supported", Pattern.CASE_INSENSITIVE);
    private static Logger log = LoggerFactory.getLogger(GlobalException.class);

    private static List<Pattern> filterPatternList;
    private static synchronized List<Pattern> getFilterPatternList() {
        if(filterPatternList == null) {
            filterPatternList = new ArrayList<>();
            filterPatternList.add(FILTER_EXCEPTION1_PATTERN);
        }
        return filterPatternList;
    }

    /**
     * 登录认证异常
     * @param ex
     * @return JsonResult
     * @author yaoyanhua
     * @date 2020/6/23 11:56
     */
    @ResponseBody
    @ExceptionHandler({UnauthenticatedException.class, AuthenticationException.class})
    public JsonResult authenticationException(AuthorizationException ex) {
        log.info(ex.getMessage() + ResponseCodeEnum.UNAUTHORIZED.getMessage(), ex);
        return JsonResult.build(ResponseCodeEnum.UNAUTHORIZED, "没有登录");
    }

    /**
     * 权限异常
     * @param ex
     * @param request
     * @param response
     * @return JsonResult
     * @author yaoyanhua
     * @date 2020/6/23 11:56
     */
    @ResponseBody
    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public JsonResult authorizationException(AuthorizationException ex, HttpServletRequest request, HttpServletResponse response) {
        log.info(ex.getMessage() + ResponseCodeEnum.UNAUTHORIZED.getMessage(), ex);
        if(HttpUtil.isAjaxRequest(request)){
            return JsonResult.build(ResponseCodeEnum.NOT_PERMISSION, "没有权限");
        }
        try {
            response.sendRedirect(request.getContextPath() + "/noPermission");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 自定义异常
     * @param e
     * @return JsonResult
     * @author yaoyanhua
     * @date 2020/6/23 11:56
     */
    @ResponseBody
    @ExceptionHandler(ConsciousException.class)
    public JsonResult handleConsciousException(ConsciousException e) {
        log.info(e.getMessage(), e);
        return JsonResult.build(e.getCode(), e.getLocalizedMessage());
    }

    /**
     * 其他异常
     * @param ex
     * @param requset
     * @param response
     * @return void
     * @author yaoyanhua
     * @date 2020/6/23 11:56
     */
    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex, HttpServletRequest requset, HttpServletResponse response) throws IOException {
        String message = ex.getLocalizedMessage();
        if(message == null || "".equals(message)){
            message = ex.getClass().getName();
        }
//        if(!isFilterException(message)){
//            log.error(ex.getMessage(), ex);
//        }
        log.error(ex.getMessage(), ex);
        message = "系统繁忙，请稍后再试";
        //直接返回json,ie浏览器会提示下载
        if(HttpUtil.isAjaxRequest(requset)){
            response.setContentType("application/json;charset=UTF-8");
        }
        else{
            response.setContentType("text/html;charset=utf-8");
        }
        response.getWriter().print(JSONObject.toJSON(JsonResult.build(ResponseCodeEnum.INTERNAL_SERVER_ERROR, message, null)));
    }

    /**
     * 验证保存的值不合格
     * @param cve cve
     * @return void
     * @author zhujinming
     * @date 10:17 20/12/18
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public void constraintViolationExceptionException(ConstraintViolationException cve, HttpServletRequest requset, HttpServletResponse response) throws IOException {
        List<String> msgList = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation : cve.getConstraintViolations()) {
            msgList.add(constraintViolation.getMessage());
        }
        String message = StringUtils.join(msgList.toArray(), ";");
        //直接返回json,ie浏览器会提示下载
        if (HttpUtil.isAjaxRequest(requset)) {
            response.setContentType("application/json;charset=UTF-8");
        } else {
            response.setContentType("text/html;charset=utf-8");
        }
        response.getWriter().print(JSONObject.toJSON(JsonResult.build(ResponseCodeEnum.INTERNAL_SERVER_ERROR, message, null)));

    }

    /**
     * 验证保存的值不合格
     * @param cve
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public JsonResult handleMethodArgumentNotValidException(MethodArgumentNotValidException cve) {
        BindingResult bindingResult = cve.getBindingResult();
        return JsonResult.fail(bindingResult.getFieldError().getDefaultMessage());
    }

    /**
     * easyExcel导入数据解析异常捕获
     *
     * @param e 数据解析异常
     * @return JsonResult<java.lang.String>
     * @author qinmingtong
     * @date 2021/1/29 9:50
     */
    @ExceptionHandler({ExcelAnalysisException.class})
    @ResponseBody
    public JsonResult<String> handleMethodArgumentNotValidException(ExcelAnalysisException  e) {
        if(e.getCause() instanceof ExcelDataConvertException){
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) e.getCause();
            CellData cellData = excelDataConvertException.getCellData();
            String errorMsg = String.format("第%s行第%s列数据有误：“%s”不合规,请修改后重新导入",
                    excelDataConvertException.getRowIndex() + 1, excelDataConvertException.getColumnIndex() + 1, cellData);
            return JsonResult.fail(errorMsg);
        }else if(e.getCause() instanceof ConsciousException){
            ConsciousException consciousException = (ConsciousException) e.getCause();
            return JsonResult.fail(consciousException.getMessage());
        }else{
            return JsonResult.fail(e.getMessage());
        }
    }

    /**
     * 是否过滤的异常，不记录到日志
     * @param err
     * @return boolean
     * @author yaoyanhua
     * @date 2020/6/23 11:56
     */
    private boolean isFilterException(String err) {
        if(StringUtils.isBlank(err)){
            return true;
        }
        if(getFilterPatternList().stream().anyMatch(p -> p.matcher(err).find())){
            return true;
        }
        return false;
    }
}
