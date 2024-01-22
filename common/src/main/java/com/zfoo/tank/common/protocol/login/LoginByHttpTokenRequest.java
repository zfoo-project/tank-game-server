
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

package com.zfoo.tank.common.protocol.login;


import com.zfoo.net.core.gateway.IGatewayLoadBalancer;
import com.zfoo.protocol.anno.Note;
import com.zfoo.protocol.anno.Protocol;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2019-10-15 17:55
 */
@Protocol(id = 1008)
public class LoginByHttpTokenRequest {

    private String token;

    @Note("0是默认登录，1为断线重连")
    private int reason;
    private String confVersion;
    private String resourceVersion;
    @Note("客户端版本号")
    private String appVersion;
    private String ip;

    public static LoginByHttpTokenRequest valueOf(String token) {
        var request = new LoginByHttpTokenRequest();
        request.token = token;
        return request;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getConfVersion() {
        return confVersion;
    }

    public void setConfVersion(String confVersion) {
        this.confVersion = confVersion;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
