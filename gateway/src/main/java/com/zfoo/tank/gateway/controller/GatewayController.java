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
import com.zfoo.net.consumer.event.ConsumerStartEvent;
import com.zfoo.net.core.gateway.model.GatewaySessionInactiveEvent;
import com.zfoo.net.router.attachment.SignalAttachment;
import com.zfoo.net.session.Session;
import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.scheduler.anno.Scheduler;
import com.zfoo.tank.common.constant.ProviderEnum;
import com.zfoo.tank.common.protocol.login.GatewayLogoutAsk;
import com.zfoo.tank.common.protocol.login.KickPlayerAsk;
import com.zfoo.tank.common.protocol.push.GatewaySessionActivePush;
import com.zfoo.tank.common.protocol.push.GatewaySessionInactivePush;
import com.zfoo.tank.common.protocol.push.GatewaySessionUpdatePush;
import com.zfoo.tank.gateway.event.GatewaySessionLoginEvent;
import com.zfoo.tank.gateway.server.MyGatewayRouteHandler;
import com.zfoo.tank.gateway.service.GatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jaysunxiao
 */
@Component
public class GatewayController {
    private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);

    @Autowired
    private GatewayService gatewayService;

    @EventReceiver
    public void onGatewaySessionLoginEvent(GatewaySessionLoginEvent event) {
        var sid = event.getSid();
        var uid = event.getUid();
        var push = new GatewaySessionActivePush(sid, uid);
        var providers = NetContext.getConsumer().findProviders(push);
        providers.stream().forEach(it -> NetContext.getRouter().send(it, push));
    }

    @EventReceiver
    public void onGatewaySessionInactiveEvent(GatewaySessionInactiveEvent event) {
        var sid = event.getSid();
        var uid = event.getUid();
        if (uid <= 0) {
            return;
        }

        // 通知push更新路由信息
        var push = new GatewaySessionInactivePush(sid, uid);
        var providers = NetContext.getConsumer().findProviders(push);
        providers.stream().forEach(it -> NetContext.getRouter().send(it, push));
        // 通知lobby玩家下线
        var ask = new GatewayLogoutAsk(uid);
        var signalAttachment = new SignalAttachment();
        signalAttachment.setTaskExecutorHash((int) uid);
        MyGatewayRouteHandler.forwardingPacket(ask, signalAttachment, uid);
        // 通知room玩家下线

    }

    /**
     * 当push消费者启动，同步网关的信息的push
     *
     * @param event 消费者启动事件
     */
    @EventReceiver
    public void onConsumerStartEvent(ConsumerStartEvent event) {
        var providerConfig = event.getProviderRegister().getProviderConfig();
        if (providerConfig == null) {
            return;
        }
        var providers = providerConfig.getProviders();
        if (CollectionUtils.isEmpty(providers)) {
            return;
        }
        if (providers.stream().noneMatch(it -> it.getProvider().equals(ProviderEnum.tankPushProvider.name()))) {
            return;
        }

        var session = event.getSession();
        var list = gatewayService.afterLoginSid2Uids();
        var push = new GatewaySessionUpdatePush(list);
        NetContext.getRouter().send(session, push);
    }


    @Scheduler(cron = "0 0/5 * * * ?")
    public void cronHourScheduler() {
        var list = gatewayService.afterLoginSid2Uids();
        var push = new GatewaySessionUpdatePush(list);

        logger.info("The gateway synchronizes session information every 5 minutes sessionSize:[{}]", list.size());

        var providers = NetContext.getConsumer().findProviders(push);
        providers.stream().forEach(it -> NetContext.getRouter().send(it, push));
    }

    @PacketReceiver
    public void atKickPlayerAsk(Session session, KickPlayerAsk ask) {
        var sid = ask.getSid();
        var uid = ask.getUid();
        var gatewaySession = NetContext.getSessionManager().getServerSession(sid);
        if (gatewaySession != null) {
            NetContext.getSessionManager().removeServerSession(gatewaySession);
        }

        // 把push服务的gateway session关联取消
        var inactiveAsk = new GatewaySessionInactivePush(sid, uid);
        var providers = NetContext.getConsumer().findProviders(inactiveAsk);
        providers.stream().forEach(it -> NetContext.getRouter().send(it, inactiveAsk));
    }

}
