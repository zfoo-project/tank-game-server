/*
 *  Copyright (C) 2020 The zfoo Authors
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.tank.home.util;

import com.zfoo.net.NetContext;
import com.zfoo.net.router.attachment.GatewayAttachment;
import com.zfoo.net.util.SessionUtils;
import com.zfoo.tank.common.entity.PlayerEntity;

/**
 * @author jaysunxiao
 */
public abstract class SendUtils {

    /**
     * 用于主动发送消息给客户端，会自己创建一个新的gatewayAttachment，如果用户没用登录，则不会发送信息
     */
    public static void sendToPlayer(PlayerEntity playerEntity, Object packet) {
        var session = NetContext.getSessionManager().getServerSession(playerEntity.getGsid().getConsumerSid());
        var uid = playerEntity.getId();

        if (uid <= 0 || !SessionUtils.isActive(session)) {
            return;
        }

        var gatewayAttachment = new GatewayAttachment(playerEntity.getGsid().getGatewaySid(), uid);
        NetContext.getRouter().send(session, packet, gatewayAttachment);
    }

}
