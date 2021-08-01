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

package com.zfoo.tank.gateway;

import com.zfoo.net.core.gateway.WebsocketGatewayServer;
import com.zfoo.net.session.model.AttributeType;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.IPacket;
import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.tank.common.protocol.login.GetPlayerInfoRequest;
import com.zfoo.tank.common.protocol.login.LoginRequest;
import com.zfoo.util.net.HostAndPort;
import com.zfoo.util.net.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.function.BiFunction;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2019-11-10 14:21
 */
public class Application {

    /**
     * tcp服务器的默认端口
     */
    public static final int TCP_SERVER_PORT = 16000;

    /**
     * websocket服务器的默认端口
     */
    public static final int WEBSOCKET_SERVER_PORT = 18000;

    public static final HostAndPort GATEWAY_HOST_AND_PORT = HostAndPort.valueOf(NetUtils.getLocalhostStr(), TCP_SERVER_PORT);
    public static final String GATEWAY_HOST_AND_PORT_STR = GATEWAY_HOST_AND_PORT.toHostAndPortStr();

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    public static final BiFunction<Session, IPacket, Boolean> packetFilter = (session, packet) -> {
        if (packet.protocolId() == LoginRequest.getProtocolId()) {
            if (session.getAttribute(AttributeType.UID) == null) {
                return false;
            } else {
                return true;
            }
        }

        if (packet.protocolId() == GetPlayerInfoRequest.getProtocolId()) {
            logger.info("[session:{}发送了GetPlayerInfo[{}]", session, packet);
            return false;
        }

        var uid = session.getAttribute(AttributeType.UID);
        if (uid != null) {
            return false;
        }

        logger.error("[session:{}发送了错误的包，因为没有登录或者是非法包[packet:{}]]", session, JsonUtils.object2String(packet));
        return true;
    };

    public static void main(String[] args) {
        var context = new ClassPathXmlApplicationContext("application.xml");
        context.registerShutdownHook();

        // tcp网关
//        var gateway = new GatewayServer(HostAndPort.valueOf(NetUtils.getLocalhostStr(), TCP_SERVER_PORT), packetFilter);
        // websocket网关
        var gateway = new WebsocketGatewayServer(HostAndPort.valueOf(NetUtils.getLocalhostStr(), WEBSOCKET_SERVER_PORT), packetFilter);
        gateway.start();
    }

}
