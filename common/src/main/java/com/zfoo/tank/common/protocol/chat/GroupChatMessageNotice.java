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
 * @since 2020-05-07 10:49
 */
public class GroupChatMessageNotice implements IPacket {

    private ChatMessage chatMessage;

    public static GroupChatMessageNotice valueOf(ChatMessage chatMessage) {
        var notice = new GroupChatMessageNotice();
        notice.chatMessage = chatMessage;
        return notice;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }
}
