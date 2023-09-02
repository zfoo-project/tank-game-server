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

import com.zfoo.event.model.AppStartEvent;
import com.zfoo.net.core.HostAndPort;
import com.zfoo.net.core.tcp.TcpServer;
import com.zfoo.net.core.websocket.WebsocketServer;
import com.zfoo.net.util.NetUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2021-01-20 16:00
 */
@SpringBootApplication
public class Application {

    /**
     * tcp服务器的默认端口
     */
    public static final int TCP_SERVER_PORT = 16000;

    /**
     * websocket服务器的默认端口
     */
    public static final int WEBSOCKET_SERVER_PORT = 18000;

    /**
     * 为什么部署的时候才用main，平时开发的时候从test启动？
     * 1.可以很好的隔离部署环境和开发测试环境
     * 2.通过main启动需要加上jvm启动参数 -Dspring.profiles.active=dev ，日志输出的路径为当前工作目录
     * 3.部署的时候从main启动（没有控制台日志），平时开发的时候从test启动（有控制台日志）
     * 4.这样正式环境的配置放在main的resources里，测试环境的配置放在test的resources里，互不干扰
     * 5.从test下启动的程序的配置文件会覆盖main中的配置文件
     *
     * logback为什么在main和test都有一份日志配置，为什么弄了两份日志配置？
     * 1.隔离生产环境配置和测试环境的日志配置
     * 2.test的配置会覆盖main中的配置
     * 3.main中没有控制台日志，test中只有控制台日志，控制台日志会加锁在生产环境会导致性能问题
     */
    public static void main(String[] args) {
        var context = SpringApplication.run(Application.class, args);

        context.registerShutdownHook();
        context.publishEvent(new AppStartEvent(context));

        // tcp服务器，可以同时启动tcp服务器和websocket服务器
         var tcpServer = new TcpServer(HostAndPort.valueOf(NetUtils.getLocalhostStr(), TCP_SERVER_PORT));
         tcpServer.start();

        // websocket服务器
        var websocketServer = new WebsocketServer(HostAndPort.valueOf(NetUtils.getLocalhostStr(), WEBSOCKET_SERVER_PORT));
        websocketServer.start();
    }

}
