/*
 * Copyright (C) 2020 The zfoo Authors
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.zfoo.tank.single.boot.controller;

import com.zfoo.net.NetContext;
import com.zfoo.net.router.receiver.PacketReceiver;
import com.zfoo.net.session.Session;
import com.zfoo.orm.anno.EntityCacheAutowired;
import com.zfoo.orm.cache.IEntityCache;
import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.storage.anno.StorageAutowired;
import com.zfoo.storage.model.IStorage;
import com.zfoo.tank.common.entity.PlayerEntity;
import com.zfoo.tank.common.protocol.TestRequest;
import com.zfoo.tank.common.protocol.TestResponse;
import com.zfoo.tank.common.resource.PlayerExpResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author jaysunxiao
 * @version 3.0
 */
@Component
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);


    @EntityCacheAutowired
    private IEntityCache<Long, PlayerEntity> playerEntityCaches;

    @StorageAutowired
    private IStorage<Integer, PlayerExpResource> playerExpStorageObject;

    @PacketReceiver
    public void atTestRequest(Session session, TestRequest request) {
        var uid = session.getUid();

        var player = playerEntityCaches.load(uid);

        logger.info("玩家[name:{}][id:{}]收到消息[{}]", player.getName(), player.getId(), JsonUtils.object2String(request));

        var playerExpResource = playerExpStorageObject.get(player.getLevel());
        logger.info("当前的钻石奖励是[{}]", playerExpResource.getDiamond());

        player.setMessage(request.getMessage());
        playerEntityCaches.update(player);

        var response = new TestResponse();
        response.message = "这是服务器的回包";

        NetContext.getRouter().send(session, response);
    }

}
