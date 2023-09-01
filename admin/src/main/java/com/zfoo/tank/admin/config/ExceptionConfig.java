/*
 * Copyright (C) 2020 The zfoo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.tank.admin.config;

import com.zfoo.tank.common.result.BaseResponse;
import com.zfoo.tank.common.result.CodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 全局异常处理，专注于服务器内部错误，如空指针异常
 *
 * @author jaysunxiao
 * @version 1.0
 * @since 2019-03-21 16:00
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
