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

package com.zfoo.tank.single.boot;

import com.zfoo.event.model.event.AppStartEvent;
import com.zfoo.net.core.tcp.TcpServer;
import com.zfoo.net.core.websocket.WebsocketServer;
import com.zfoo.tank.common.constant.TankDeployEnum;
import com.zfoo.util.ThreadUtils;
import com.zfoo.util.net.HostAndPort;
import com.zfoo.util.net.NetUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2021-01-20 16:36
 */
@Ignore
@SpringBootApplication(exclude = {
        // 排除MongoDB自动配置
        MongoDataAutoConfiguration.class,
        MongoRepositoriesAutoConfiguration.class,
        MongoAutoConfiguration.class,
        MongoReactiveAutoConfiguration.class,
        MongoReactiveDataAutoConfiguration.class,
        MongoReactiveRepositoriesAutoConfiguration.class,

        TaskExecutionAutoConfiguration.class,
        TaskSchedulingAutoConfiguration.class
})
public class ApplicationTest {

    static {
        TankDeployEnum.initDefaultEnv();
    }

    // unity编辑器运行游戏
    @Test
    public void startApplication() {
        var context = SpringApplication.run(ApplicationTest.class);

        context.registerShutdownHook();
        context.publishEvent(new AppStartEvent(context));

        var tcpServer = new TcpServer(HostAndPort.valueOf(NetUtils.LOCAL_LOOPBACK_IP, Application.TCP_SERVER_PORT));
        tcpServer.start();

        ThreadUtils.sleep(Long.MAX_VALUE);
    }

    // 在浏览器中运行游戏
    @Test
    public void startWebsocketApplication() {
        var context = SpringApplication.run(ApplicationTest.class);
        context.registerShutdownHook();
        context.publishEvent(new AppStartEvent(context));

        var websocketServer = new WebsocketServer(HostAndPort.valueOf(NetUtils.LOCAL_LOOPBACK_IP, Application.WEBSOCKET_SERVER_PORT));
        websocketServer.start();

        ThreadUtils.sleep(Long.MAX_VALUE);
    }

}
