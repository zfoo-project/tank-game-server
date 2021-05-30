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

package com.zfoo.tank.common.result;

import org.springframework.lang.Nullable;

/**
 * http请求的通用返回
 */
public class BaseResponse {

    private int code;
    private String message;
    private Object data;

    public static BaseResponse valueOf(int code) {
        var response = new BaseResponse();
        response.code = code;
        return response;
    }

    public static BaseResponse valueOf(CodeEnum code) {
        var response = new BaseResponse();
        response.code = code.getCode();
        response.message = code.getMessage();
        return response;
    }

    public static BaseResponse valueOf(CodeEnum code, @Nullable Object data) {
        var response = new BaseResponse();
        response.code = code.getCode();
        response.message = code.getMessage();
        response.data = data;
        return response;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
