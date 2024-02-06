package com.zfoo.tank.gateway.controller;

import com.zfoo.net.NetContext;
import com.zfoo.net.anno.PacketReceiver;
import com.zfoo.net.session.Session;
import com.zfoo.tank.common.protocol.push.CommonNoticePush;
import com.zfoo.tank.gateway.service.GatewayRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PushGatewayController {

    private static final Logger logger = LoggerFactory.getLogger(PushGatewayController.class);

    @Autowired
    private GatewayRoomService gatewayRoomService;

    @PacketReceiver
    public void atCommonNoticePush(Session session, CommonNoticePush push) {
        var targets = push.getTargets();
        var notices = push.allNotices();
        logger.info("atCommonNoticePush targets:[{}] noticeSize:[{}]", targets, notices.size());
        for (var sid : targets) {
            var pushSession = NetContext.getSessionManager().getServerSession(sid);
            for (var notice : notices) {
                NetContext.getRouter().send(pushSession, notice, null);
            }
        }
    }
}
