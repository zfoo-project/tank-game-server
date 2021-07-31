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

package com.zfoo.tank.common.constant;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2021-01-20 14:58
 */
public enum I18nEnum {

    error_0("请求错误0"),
    error_1("请求错误1"),
    error_2("请求错误2"),
    error_3("请求错误3"),
    error_4("请求错误4"),
    error_5("请求错误5"),
    error_6("请求错误6"),

    error_unknown("未知错误，请联系客服，我们会尽快解决"),
    error_protocol_param("协议参数错误，请联系客服，我们会尽快解决"),
    error_protocol_parse("无法解析，请联系客服，我们会尽快解决"),
    error_account_not_exist("账号不存在"),
    error_account_password("账号或密码错误"),
    error_account_sensitive_word("账号含有铭感词"),

    fail("请求失败"),
    ok("请求成功"),

    ok_quietly("请求成功，但是不会在客户端上显示提示"),

    ;


    private String message;

    I18nEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
