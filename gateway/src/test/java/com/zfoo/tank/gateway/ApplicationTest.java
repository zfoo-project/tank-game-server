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

import com.zfoo.net.core.HostAndPort;
import com.zfoo.net.util.NetUtils;
import com.zfoo.protocol.util.ThreadUtils;
import com.zfoo.tank.common.constant.TankDeployEnum;
import com.zfoo.tank.gateway.server.MyGatewayServer;
import com.zfoo.tank.gateway.server.MyWebsocketGatewayServer;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2021-01-20 16:36
 */
@Ignore
public class ApplicationTest {

    static {
        TankDeployEnum.initDefaultEnv();
    }

    // unity编辑器运行游戏
    @Test
    public void startApplication() {
        var context = new ClassPathXmlApplicationContext("application.xml");
        context.registerShutdownHook();

        // tcp网关
        var tcpGateway = new MyGatewayServer(HostAndPort.valueOf(NetUtils.LOCAL_LOOPBACK_IP, Gateway.TCP_SERVER_PORT));
        tcpGateway.start();

        ThreadUtils.sleep(Long.MAX_VALUE);
    }

    // 在浏览器中运行游戏
    @Test
    public void startWebsocketApplication() {
        var context = new ClassPathXmlApplicationContext("application.xml");
        context.registerShutdownHook();

        // websocket网关
        var websocketGateway = new MyWebsocketGatewayServer(HostAndPort.valueOf(NetUtils.getLocalhostStr(), Gateway.WEBSOCKET_SERVER_PORT));
        websocketGateway.start();

        ThreadUtils.sleep(Long.MAX_VALUE);
    }

}
