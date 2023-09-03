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

package com.zfoo.tank.common.protocol.chat;

import com.zfoo.net.packet.IPacket;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2019-11-17 11:43
 */
public class ChatMessage implements IPacket {

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

    public static ChatMessage valueOf(long id, long sendId, String avatar, String name, String message, long timestamp) {
        var chatMessage = new ChatMessage();
        chatMessage.id = id;
        chatMessage.sendId = sendId;
        chatMessage.avatar = avatar;
        chatMessage.name = name;
        chatMessage.message = message;
        chatMessage.timestamp = timestamp;
        return chatMessage;
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
