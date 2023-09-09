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
import com.zfoo.protocol.anno.Protocol;
import com.zfoo.tank.common.util.TokenUtils;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2019-10-15 17:55
 */
@Protocol(id = 1004)
public class GetPlayerInfoRequest implements IGatewayLoadBalancer {

    private String token;

    public static GetPlayerInfoRequest valueOf(String token) {
        var packet = new GetPlayerInfoRequest();
        packet.token = token;
        return packet;
    }

    @Override
    public Object loadBalancerConsistentHashObject() {
        var triple = TokenUtils.get(token);
        return triple.getLeft();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
