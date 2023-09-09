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

import com.zfoo.protocol.anno.Protocol;

import java.util.List;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2019-11-14 10:54
 */
@Protocol(id = 4004)
public class GroupHistoryMessageResponse {

    private List<ChatMessage> chatMessages;

    public static GroupHistoryMessageResponse valueOf(List<ChatMessage> chatMessages) {
        var response = new GroupHistoryMessageResponse();
        response.chatMessages = chatMessages;
        return response;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }
}
