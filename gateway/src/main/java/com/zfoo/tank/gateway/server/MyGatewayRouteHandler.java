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

package com.zfoo.tank.gateway.server;

import com.zfoo.event.manager.EventBus;
import com.zfoo.net.NetContext;
import com.zfoo.net.core.gateway.IGatewayLoadBalancer;
import com.zfoo.net.core.gateway.model.GatewaySessionInactiveEvent;
import com.zfoo.net.handler.ServerRouteHandler;
import com.zfoo.net.packet.DecodedPacketInfo;
import com.zfoo.net.packet.common.Heartbeat;
import com.zfoo.net.packet.common.Ping;
import com.zfoo.net.packet.common.Pong;
import com.zfoo.net.router.attachment.GatewayAttachment;
import com.zfoo.net.router.attachment.SignalAttachment;
import com.zfoo.net.session.Session;
import com.zfoo.net.util.SessionUtils;
import com.zfoo.scheduler.util.TimeUtils;
import com.zfoo.tank.common.protocol.login.LoginByHttpTokenRequest;
import com.zfoo.tank.common.protocol.login.LoginRequest;
import com.zfoo.tank.common.result.CodeEnum;
import com.zfoo.tank.common.util.HttpLoginUtils;
import com.zfoo.tank.gateway.event.GatewaySessionLoginEvent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author godotg
 */
@ChannelHandler.Sharable
public class MyGatewayRouteHandler extends ServerRouteHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyGatewayRouteHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 请求者的session，一般是serverSession
        var session = SessionUtils.getSession(ctx);
        if (session == null) {
            return;
        }

        var decodedPacketInfo = (DecodedPacketInfo) msg;
        var packet = decodedPacketInfo.getPacket();
        var signalAttachment = (SignalAttachment) decodedPacketInfo.getAttachment();
        if (filterPacket(session, packet, signalAttachment)) {
            return;
        }

        // 把客户端信息包装为一个GatewayAttachment,因此通过这个网关附加包可以得到玩家的uid、sid之类的信息
        var attachment = new GatewayAttachment(session);
        if (signalAttachment != null) {
            signalAttachment.setClient(SignalAttachment.SIGNAL_SERVER);
            attachment.setSignalAttachment(signalAttachment);
        }

        var uid = session.getUid();
        if (uid <= 0) {
            NetContext.getRouter().send(session, CodeEnum.error(CodeEnum.login_first), attachment);
            return;
        }


        // 网关优先使用IGatewayLoadBalancer作为一致性hash的计算参数，然后才会使用客户端的session做参数
        // 例子：以聊天服务来说，玩家知道自己在哪个群组groupId中，那往这个群发送消息时，会在Packet中带上这个groupId做为一致性hash就可以了。
        if (packet instanceof IGatewayLoadBalancer) {
            var loadBalancerConsistentHashObject = ((IGatewayLoadBalancer) packet).loadBalancerConsistentHashObject();
            attachment.setTaskExecutorHash(loadBalancerConsistentHashObject.hashCode());
            forwardingPacket(packet, attachment, loadBalancerConsistentHashObject);
            return;
        }

        forwardingPacket(packet, attachment, uid);
    }

    public boolean filterPacket(Session session, Object packet, Object attachment) {
        var packetClass = packet.getClass();
        if (packetClass == Heartbeat.class) {
            return true;
        }
        if (packetClass == Ping.class) {
            NetContext.getRouter().send(session, Pong.valueOf(TimeUtils.now()), attachment);
            return true;
        }

        if (packetClass == LoginByHttpTokenRequest.class) {
            var loginRequest = (LoginByHttpTokenRequest) packet;
            var loginInfo = HttpLoginUtils.fromToken(loginRequest.getToken());
            var expireTime = loginInfo.getExpireTime();
            if (TimeUtils.now() >= expireTime) {
                NetContext.getRouter().send(session, CodeEnum.error(CodeEnum.login_token_expire), attachment);
                return true;
            }
            var uid = loginInfo.getUid();
            // gatewayLogin
            session.setUid(uid);
            EventBus.post(GatewaySessionLoginEvent.valueOf(session.getSid(), uid));
            return false;
        }

        return false;
    }


    /**
     * 转发网关收到的包到Provider
     */
    public static void forwardingPacket(Object packet, Object attachment, Object argument) {
        try {
            // 网关统一用 moduleid uid 获取 session
            var providers = NetContext.getConsumer().findProviders(packet);
            var loadBalancer = NetContext.getConsumer().selectLoadBalancer(providers, packet);
            var providerSession = loadBalancer.selectProvider(providers, packet, argument);
            NetContext.getRouter().send(providerSession, packet, attachment);
        } catch (Exception e) {
            logger.error("An exception occurred at the gateway", e);
        } catch (Throwable t) {
            logger.error("An error occurred at the gateway", t);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        var session = SessionUtils.getSession(ctx);
        if (session == null) {
            return;
        }
        var sid = session.getSid();
        var uid = session.getUid();
        // 连接到网关的客户端断开了连接
        EventBus.post(GatewaySessionInactiveEvent.valueOf(sid, uid));
        super.channelInactive(ctx);
    }

}
