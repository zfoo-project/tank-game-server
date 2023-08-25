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

package com.zfoo.tank.common.entity;


import com.zfoo.orm.model.anno.EntityCache;
import com.zfoo.orm.model.anno.Id;
import com.zfoo.orm.model.entity.IEntity;
import com.zfoo.tank.common.protocol.chat.ChatMessage;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2019-11-17 11:43
 */
@EntityCache
public class MessageEntity implements IEntity<Long> {

    /**
     * 消息的唯一id
     */
    @Id
    private long id;

    /**
     * 发送者的id
     */
    private long sendId;

    private String avatar;

    private String name;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 发送的时间戳
     */
    private long timestamp;


    public static MessageEntity valueOf(long id, long sendId, String avatar, String name, String message, long timestamp) {
        var entity = new MessageEntity();
        entity.id = id;
        entity.sendId = sendId;
        entity.avatar = avatar;
        entity.name = name;
        entity.message = message;
        entity.timestamp = timestamp;
        return entity;
    }

    public ChatMessage toChatMessage() {
        return ChatMessage.valueOf(id, sendId, avatar, name, message, timestamp);
    }

    @Override
    public Long id() {
        return id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSendId() {
        return sendId;
    }

    public void setSendId(long sendId) {
        this.sendId = sendId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
