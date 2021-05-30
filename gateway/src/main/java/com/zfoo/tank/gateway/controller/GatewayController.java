/*
 *  Copyright (C) 2020 The zfoo Authors
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.tank.gateway.controller;

import com.zfoo.event.model.anno.EventReceiver;
import com.zfoo.net.NetContext;
import com.zfoo.net.core.gateway.model.GatewaySessionInactiveEvent;
import com.zfoo.net.packet.model.GatewayPacketAttachment;
import com.zfoo.tank.common.protocol.login.LogoutRequest;
import org.springframework.stereotype.Component;

/**
 * @author jaysunxiao
 * @version 3.0
 */
@Component
public class GatewayController {

    @EventReceiver
    public void onGatewaySessionInactiveEvent(GatewaySessionInactiveEvent event) {
        var sid = event.getSid();
        var uid = event.getUid();

        if (uid <= 0) {
            return;
        }

        var packet = LogoutRequest.valueOf();

        var loadBalancer = NetContext.getConfigManager().consumerLoadBalancer();
        var consumerSession = loadBalancer.loadBalancer(packet, uid);

        // 玩家登出
        var gatewayAttachment = new GatewayPacketAttachment(sid, uid);
        gatewayAttachment.setClient(true);
        NetContext.getDispatcher().send(consumerSession, packet, gatewayAttachment);
    }

}
