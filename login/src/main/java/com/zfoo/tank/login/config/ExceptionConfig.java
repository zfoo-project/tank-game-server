package com.zfoo.tank.login.config;

import com.zfoo.tank.common.result.BaseResponse;
import com.zfoo.tank.common.result.CodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理，专注于服务器内部错误，如空指针异常
 *
 * @author sun
 */
@ControllerAdvice
public class ExceptionConfig {

    private final Logger logger = LoggerFactory.getLogger(ExceptionConfig.class);

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public BaseResponse nullPointerExceptionHandler(HttpServletRequest request, HttpServletResponse response, NullPointerException exception) {
        return handleErrorInfo(request, response, exception);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        return handleErrorInfo(request, response, exception);
    }

    private BaseResponse handleErrorInfo(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        logger.error("request url:[{}] has an exception", request.getRequestURI(), exception);
        // 发生错误时，不要返回 200 状态码，使用通用的状态码
        return BaseResponse.valueOf(CodeEnum.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

}
