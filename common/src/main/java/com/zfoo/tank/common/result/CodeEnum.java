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

/**
 * HTTP状态码规范，英文名称，code，message都符合标准规范
 * 1xx：相关信息
 * 2xx：操作成功
 * 3xx：重定向
 * 4xx：客户端错误
 * 5xx：服务器错误
 *
 * @author jaysunxiao
 * @version 1.0
 * @since 2019-03-21 16:30
 */
public enum CodeEnum {

    /**
     * 通用错误码
     */
    FAIL_TWO(-2, "请求失败-2"),
    FAIL_ONE(-1, "请求失败-1"),
    FAIL(0, "请求失败"),
    OK(1, "请求成功"),
    OK_QUIETLY(2, "请求成功，但是不会在客户端上显示提示"),

    PARAMETER_ERROR(10, "请求参数非法"),
    PARAMETER_ERROR_ONE(11, "请求参数非常非法"),
    PARAMETER_ERROR_TWO(12, "请求参数极度非法"),
    PARAMETER_ERROR_THREE(13, "请求参数变态非法"),
    PARAMETER_ERROR_FOUR(14, "请求参数无法形容非法"),
    PARAMETER_EMPTY(15, "空的请求参数"),
    PARAMETER_EMPTY_ONE(16, "非常空的请求参数"),
    PARAMETER_EMPTY_TWO(17, "极度空的请求参数"),
    PARAMETER_NOT_MATCH(20, "参数无法匹配"),
    PARAMETER_URL_ERROR(25, "url不合法"),
    PARAMETER_ENV_ERROR(26, "为了安全，此zfoo环境暂时屏蔽热更新，你自己的正式环境中可以打开"),

    PARAMETER_WORD_FILTER_ERROR(30, "检测到敏感字符"),

    /**
     * 500-600为服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器遇到了一个未曾预料的状况，导致了它无法完成对请求的处理"),
    NOT_IMPLEMENTED(501, "服务器无法识别请求的方法，并且无法支持其对任何资源的请求"),
    BAD_GATEWAY(502, "作为网关或者代理工作的服务器尝试执行请求时，从上游服务器接收到无效的响应"),
    SERVICE_UNAVAILABLE(503, "临时的服务器维护或者过载，服务器当前无法处理请求"),
    GATEWAY_TIMEOUT(504, "网关或者代理工作的服务器尝试执行请求时"),
    HTTP_VERSION_NOT_SUPPORTED(505, "服务器不支持，或者拒绝支持在请求中使用的 HTTP 版本"),
    VARIANT_ALSO_NEGOTIATES(506, "服务器存在内部配置错误：透明内容协商协议"),
    INSUFFICIENT_STORAGE(507, "服务器无法存储完成请求所必须的内容"),
    BANDWIDTH_LIMIT_EXCEEDED(509, "服务器达到带宽限制，这不是一个官方的状态码，但是仍被广泛使用"),
    NOT_EXTENDED(510, "获取资源所需要的策略并没有被满足"),


    /**
     * 1000-1100为登录相关
     */
    SIGN_IN_FAIL(1000, "账号密码错误"),
    SIGN_IN_FIRST(1001, "请先登录"),
    SIGN_IN_VERIFY_CODE_ERROR(1002, "验证码不正确"),
    SIGN_IN_VERIFY_CODE_NOT_EXIST(1003, "图片验证码过期"),
    SIGN_IN_VERIFY_CODE_CALCULATE_ERROR(1004, "验证码计算错误"),
    SIGN_IN_PHONE_NUMBER_ERROR(1005, "手机号码错误"),
    SIGN_IN_PHONE_NUMBER_LIMIT_ERROR(1006, "验证码发送数量超过限制"),
    SIGN_IN_PHONE_CODE_NOT_EXIST(1007, "手机验证码过期"),
    SIGN_IN_PHONE_CODE_ERROR(1008, "手机验证码不正确"),
    SIGN_IN_REGISTER_CODE_NOT_EXIST(1009, "验证过期，请重新验证"),

    USER_NOT_EXIST(1010, "用户不存在"),
    COIN_NOT_ENOUGH(1011, "z币不足"),
    STAR_MYSELF_ERROR(1012, "用户无法关注自己本身"),

    USER_AVATAR_PATH_ERROR(1020, "用户上传头像路径错误"),
    USER_BACKGROUND_PATH_ERROR(1021, "用户上传背景路径错误"),
    USER_BIND_WEIBO_ERROR(1022, "该微博已经绑定到了其它用户"),
    USER_BIND_PHONE_ERROR(1023, "该手机号已经绑定到了其它用户"),
    USER_ALREADY_BIND_WEIBO_ERROR(1024, "该账号已经绑定了微博"),
    USER_BIND_WEIBO_EMPTY_ERROR(1025, "该账户没有绑定任何微博"),
    USER_BIND_WECHAT_ERROR(1026, "该微信已经绑定到了其它用户"),
    USER_ALREADY_BIND_WECHAT_ERROR(1027, "该账号已经绑定了微博"),
    USER_BIND_WECHAT_EMPTY_ERROR(1028, "该账户没有绑定任何微信"),

    ;

    private int code;
    private String message;

    CodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
