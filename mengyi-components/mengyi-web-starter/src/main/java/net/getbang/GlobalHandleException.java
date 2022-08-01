package net.getbang;

import net.getbang.core.base.CommonResult;
import net.getbang.core.enums.ResponseCode;
import net.getbang.core.util.ServletUtils;
import net.getbang.core.util.StringUtils;
import net.getbang.log.publisher.ErrorLogPublisher;
import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;

@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice
public class GlobalHandleException {


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult handleError(MissingServletRequestParameterException e) {
        log.warn("缺少请求参数", e.getMessage());
        String message = String.format("缺少必要的请求参数: %s", e.getParameterName());
        return CommonResult.error(ResponseCode.PARAM_ERROR.code(), message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult handleError(MethodArgumentTypeMismatchException e) {
        log.warn("请求参数格式错误", e.getMessage());
        String message = String.format("请求参数格式错误: %s", e.getName());
        return CommonResult.error(ResponseCode.PARAM_ERROR.code(), message);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult handleError(Throwable e) {
        log.error("服务器异常", e);
        //发送服务异常事件
        ErrorLogPublisher.publishEvent(e, URLUtil.getPath(ServletUtils.getRequest().getRequestURI()));
        return CommonResult.error(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), (StringUtils.isEmpty(e.getMessage()) ? ResponseCode.INTERNAL_SERVER_ERROR.getDesc() : e.getMessage()));
    }










}
