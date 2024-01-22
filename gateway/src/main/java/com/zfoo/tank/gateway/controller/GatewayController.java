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

import com.zfoo.event.anno.EventReceiver;
import com.zfoo.net.NetContext;
import com.zfoo.net.anno.PacketReceiver;
import com.zfoo.net.core.gateway.model.GatewaySessionInactiveEvent;
import com.zfoo.net.router.attachment.GatewayAttachment;
import com.zfoo.net.session.Session;
import com.zfoo.tank.common.protocol.login.GatewayLogoutAsk;
import com.zfoo.tank.common.protocol.login.KickPlayerAsk;
import com.zfoo.tank.common.protocol.login.LogoutRequest;
import com.zfoo.tank.gateway.event.GatewaySessionLoginEvent;
import com.zfoo.tank.gateway.server.MyGatewayRouteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author jaysunxiao
 */
@Component
public class GatewayController {
    private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);
    @EventReceiver
    public void onGatewaySessionLoginEvent(GatewaySessionLoginEvent event) {
        var uid = event.getUid();
        var sid = event.getSid();

        logger.info("网关用户登录 [uid:{}] [sid:{}]", uid, sid);
    }

    @EventReceiver
    public void onGatewaySessionInactiveEvent(GatewaySessionInactiveEvent event) {
        var sid = event.getSid();
        var uid = event.getUid();

        if (uid <= 0) {
            return;
        }

        var packet = LogoutRequest.valueOf();

        var providers = NetContext.getConsumer().findProviders(packet);
        var loadBalancer = NetContext.getConsumer().selectLoadBalancer(providers, packet);
        var providerSession = loadBalancer.selectProvider(providers, packet, uid);

        // 包的附加包，通过网关转发到home的包会丢失sid和uid，通过这个GatewayAttachment附带到IPacket后面，home就知道哪个玩家发的包了
        // 玩家登出
        var gatewayAttachment = new GatewayAttachment(sid, uid);
        gatewayAttachment.setClient(true);
        NetContext.getRouter().send(providerSession, packet, gatewayAttachment);
    }



    @PacketReceiver
    public void atKickPlayerAsk(Session session, KickPlayerAsk ask) {
        var sid = ask.getSid();
        var uid = ask.getUid();
        var gatewaySession = NetContext.getSessionManager().getServerSession(sid);
        if (gatewaySession != null) {
            NetContext.getSessionManager().removeServerSession(gatewaySession);
        }
    }

}
