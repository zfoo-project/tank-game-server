package com.zfoo.tank.common.result;

import com.zfoo.net.packet.common.Error;


/**
 * HTTP状态码规范，英文名称，code，message都符合标准规范
 * 1xx：相关信息
 * 2xx：操作成功
 * 3xx：重定向
 * 4xx：客户端错误
 * 5xx：服务器错误
 *
 * @author sun
 */
public enum CodeEnum {

    /**
     * 通用错误码
     */
    FAIL(0, "请求失败"),
    OK(1, "请求成功"),

    PARAMETER_ERROR(10, "请求参数非法"),
    PARAMETER_ENV_ERROR(26, "为了安全，此zfoo环境暂时屏蔽热更新，你自己的正式环境中可以打开"),

    /**
     * 500-600为服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器遇到了一个未曾预料的状况，导致了它无法完成对请求的处理"),

    /**
     * 600-700为通用错误
     */
    error_config(600, "配置错误"),
    error_1(601, "未知错误（1），请联系客服，我们会尽快解决"),
    error_2(602, "协议参数错误（2），请联系客服，我们会尽快解决"),
    error_3(603,"协议错误（3），请联系客服，我们会尽快解决"),
    error_4(604,"无法解析（4），请联系客服，我们会尽快解决"),
    error_5(605,"无法认证（5），请联系客服，我们会尽快解决"),
    error_6(606,"没有账号（6），请联系客服，我们会尽快解决"),
    error_7(607,"没有账号名字（7），请联系客服，我们会尽快解决"),
    error_32(608,"账号或密码错误"),
    error_33(609,"检测到敏感字符"),
    error_34(610,"玩家处于保护盾状态，不能被攻打"),
    error_37(611,"用户不存在"),
    error_38(612,"服务器尚未开放"),
    error_100(612,"请求错误0"),
    error_101(613,"请求错误1"),
    error_102(614,"请求错误2"),
    error_103(615,"请求错误3"),
    error_104(616,"请求错误4"),
    error_105(617,"请求错误5"),
    error_107(618,"目标今日已被攻陷多次，正处于保护盾中"),

    /**
     * 1000-1100为登录相关
     */
    login_12(1000, "请先登录"),
    login_13(1001, "账号已在别处登录"),
    login_15(1002, "账号不能为空"),
    login_16(1003, "密码不能为空"),
    login_17(1004, "账号只能包含英文，数字，常用符号"),
    login_18(1005, "密码只能包含英文，数字，常用符号"),
    login_19(1006, "账号已存在"),
    login_20(1007, "用户名的中文最大长度8个字符，英文最大长度16个字符"),
    login_21(1008, "名称重复"),
    login_22(1009, "不能包含特殊字符"),
    login_23(1010, "您已被封号到{0}"),
    login_24(1011, "重连失败，账号已在别处登录"),
    login_25(1012, "名字不能为空"),
    login_26(1013, "您已经创建角色"),
    login_27(1014, "注册人数已满"),
    login_token_expire(1015, "登录token已经过期，请重新登录"),
    login_first(1016, "请先登录"),

    /**
     * 1100-1200为房间相关
     */
    room_max_num_limit(1100, "超过房间最大人数限制"),
    room_join_repeatedly(1101, "重复加入房间"),
    room_owner_limit(1102, "只有房主有权限"),
    room_server_limit(1103, "房间服务器不存在"),
    room_no_resource(1104, "房间服务器资源不够"),
    room_creating(1105, "正在创建房间请稍等"),
    room_no_player(1106, "玩家不在房间"),
    room_player_range_limit(1107, "房间人数不在合理的范围内"),
    ;

    private final int code;
    private final String message;

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

    public static Error error(CodeEnum codeEnum) {
        var error = Error.valueOf(codeEnum.getCode(), codeEnum.getMessage());
        return error;
    }
}
