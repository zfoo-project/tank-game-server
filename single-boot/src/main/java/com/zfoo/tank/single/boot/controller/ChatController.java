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
 */

package com.zfoo.tank.single.boot.controller;

import com.mongodb.client.model.Filters;
import com.zfoo.net.NetContext;
import com.zfoo.net.router.receiver.PacketReceiver;
import com.zfoo.net.session.Session;
import com.zfoo.orm.OrmContext;
import com.zfoo.orm.anno.EntityCacheAutowired;
import com.zfoo.orm.cache.IEntityCaches;
import com.zfoo.orm.util.MongoIdUtils;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.scheduler.util.TimeUtils;
import com.zfoo.tank.common.entity.MessageEntity;
import com.zfoo.tank.common.entity.PlayerEntity;
import com.zfoo.tank.common.protocol.chat.GroupChatMessageNotice;
import com.zfoo.tank.common.protocol.chat.GroupChatRequest;
import com.zfoo.tank.common.protocol.chat.GroupHistoryMessageRequest;
import com.zfoo.tank.common.protocol.chat.GroupHistoryMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author godotg
 * @version 3.0
 */
@Component
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @EntityCacheAutowired
    private IEntityCaches<Long, PlayerEntity> playerEntityCaches;

    @PacketReceiver
    public void atGroupChatRequest(Session session, GroupChatRequest request) {
        var uid = session.getUid();

        var message = request.getMessage();
        if (StringUtils.isBlank(message)) {
            return;
        }

        var player = playerEntityCaches.load(uid);

        var uuid = MongoIdUtils.getIncrementIdFromMongoDefault(MessageEntity.class);
        var entity = MessageEntity.valueOf(uuid, player.getId(), StringUtils.EMPTY, player.getName(), message, TimeUtils.now());
        OrmContext.getAccessor().insert(entity);

        var notice = GroupChatMessageNotice.valueOf(entity.toChatMessage());
        // 广播给全服玩家
        NetContext.getSessionManager().forEachServerSession(it -> NetContext.getRouter().send(it, notice));
    }

    @PacketReceiver
    public void atGroupHistoryMessageRequest(Session session, GroupHistoryMessageRequest cm) {
        var lastMessageId = cm.getLastMessageId();

        var collection = OrmContext.getOrmManager().getCollection(MessageEntity.class);
        if (lastMessageId <= 0) {
            lastMessageId = collection.countDocuments() + 1;
        }

        var list = new ArrayList<MessageEntity>();
        OrmContext.getOrmManager()
                .getCollection(MessageEntity.class)
                .find(Filters.lt("_id", lastMessageId))
                .limit(300)
                .forEach(it -> list.add(it));

        NetContext.getRouter().send(session, GroupHistoryMessageResponse.valueOf(list.stream().map(it -> it.toChatMessage()).collect(Collectors.toList())));
    }


}
