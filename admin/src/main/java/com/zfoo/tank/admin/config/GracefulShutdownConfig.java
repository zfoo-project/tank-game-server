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

package com.zfoo.tank.admin.config;

import com.zfoo.protocol.exception.ExceptionUtils;
import com.zfoo.protocol.util.ThreadUtils;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.Executor;

/**
 * kill -2 和 kill -15 才能触发这个回调方法
 *
 * @author jaysunxiao
 * @version 1.0
 * @since 2019-03-10 19:43
 */
@Configuration
public class GracefulShutdownConfig implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(GracefulShutdownConfig.class);

    private volatile Connector connector;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers(this);
        logger.info("Deploy graceful shutdown config successfully!");
        return tomcat;
    }

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        // 关闭Tomcat线程池
        try {
            this.shutdownTomcat();
        } catch (Throwable e) {
            logger.error("Tomcat thread pool failed shutdown: " + ExceptionUtils.getMessage(e));
            return;
        }
        logger.info("Tomcat shutdown gracefully.");
    }

    private void shutdownTomcat() {
        this.connector.pause();
        Executor executor = this.connector.getProtocolHandler().getExecutor();
        if (executor instanceof ThreadPoolExecutor) {
            var threadPoolExecutor = (ThreadPoolExecutor) executor;
            ThreadUtils.shutdown(threadPoolExecutor);
        }
    }

}
