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

package com.zfoo.tank.gateway.controller;


import com.zfoo.event.manager.EventBus;
import com.zfoo.event.model.event.AppStartEvent;
import com.zfoo.net.NetContext;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.scheduler.model.anno.Scheduler;
import com.zfoo.tank.common.constant.GameConstant;
import com.zfoo.tank.gateway.Application;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2020-07-20 13:58
 */
@Component
public class StatisticsController implements ApplicationListener<AppStartEvent> {


    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);


    @Override
    public void onApplicationEvent(AppStartEvent appStartEvent) {
        var path = StringUtils.format("{}/{}", GameConstant.USER_ONLINE_ID_ZK_PATH, Application.GATEWAY_HOST_AND_PORT);

        NetContext.getConfigManager().getRegistry().addData(path, StringUtils.EMPTY_BYTES, CreateMode.EPHEMERAL);

    }

    /**
     * 每分钟刷新任务
     */
    @Scheduler(cron = "0 * * * * ?")
    public void cronMinuteScheduler() {
        logger.info("每分钟上报玩家数量开始:{}", NetContext.getSessionManager().getServerSessionMap().size());
        EventBus.asyncExecute().execute(() -> {
            var path = StringUtils.format("{}/{}", GameConstant.USER_ONLINE_ID_ZK_PATH, Application.GATEWAY_HOST_AND_PORT);

            NetContext.getConfigManager().getRegistry().addData(path
                    , StringUtils.bytes(String.valueOf(NetContext.getSessionManager().getServerSessionMap().size()))
                    , CreateMode.EPHEMERAL);
        });
    }


}
