package com.zfoo.tank.push.controller;

import com.zfoo.net.NetContext;
import com.zfoo.net.anno.PacketReceiver;
import com.zfoo.net.session.Session;
import com.zfoo.net.util.SessionUtils;
import com.zfoo.tank.push.service.PushService;
import com.zfoo.tank.common.protocol.push.CommonNoticePush;
import com.zfoo.tank.common.protocol.transfer.AdminToRoomTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PushRoomController {

    private static final Logger logger = LoggerFactory.getLogger(PushRoomController.class);

    @Autowired
    private PushService pushService;

    @PacketReceiver
    public void atCommonNoticePush(Session session, CommonNoticePush push) throws CloneNotSupportedException {
        var targets = push.getTargets();
        logger.info("atCommonNoticePush targets:[{}]", targets);
        var map = pushService.uidToGatewaySid(targets);
        for (var entry : map.entrySet()) {
            var gatewaySession = entry.getKey();
            var sids = entry.getValue();
            var pushToGateway = (CommonNoticePush) push.clone();
            pushToGateway.setTargets(sids);
            NetContext.getRouter().send(gatewaySession, pushToGateway);
        }
    }


    @PacketReceiver
    public void atAdminToRoomTransfer(Session session, AdminToRoomTransfer transfer) {
        logger.info("atAdminToRoomTransfer roomId:[{}]", transfer.getRoomId());
        // 找个网关发过去，网关可以知道room在哪里
        for (var gatewaySid2ConsumerSid : pushService.gatewaySessionCaches.asMap().values()) {
            var gatewaySid = gatewaySid2ConsumerSid.getGatewaySid();
            var consumerSid = gatewaySid2ConsumerSid.getConsumerSid();
            var gatewaySession = NetContext.getSessionManager().getServerSession(consumerSid);
            if (!SessionUtils.isActive(gatewaySession)) {
                continue;
            }
            // 给一个网关转发消息就可以了
            NetContext.getRouter().send(gatewaySession, transfer);
            break;
        }
    }
}
