package com.zfoo.tank.push.controller;

import com.zfoo.net.anno.PacketReceiver;
import com.zfoo.net.session.Session;
import com.zfoo.tank.push.service.PushService;
import com.zfoo.tank.common.protocol.push.GatewaySessionActivePush;
import com.zfoo.tank.common.protocol.push.GatewaySessionInactivePush;
import com.zfoo.tank.common.protocol.push.GatewaySessionUpdatePush;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PushController {

    private static final Logger logger = LoggerFactory.getLogger(PushController.class);

    @Autowired
    private PushService pushService;

    @PacketReceiver
    public void atGatewaySessionActivePush(Session session, GatewaySessionActivePush push) {
        logger.info("atGatewaySessionActivePush sid:[{}] uid:[{}] ", push.getSid(), push.getUid());
        pushService.addGatewaySid(session, push.getSid(), push.getUid());
    }

    @PacketReceiver
    public void atGatewaySessionInactivePush(Session session, GatewaySessionInactivePush push) {
        logger.info("atGatewaySessionInactivePush sid:[{}] uid:[{}]", push.getSid(), push.getUid());
        pushService.removeGatewaySid(session, push.getSid(), push.getUid());
    }

    @PacketReceiver
    public void atGatewaySessionUpdatePush(Session session, GatewaySessionUpdatePush push) {
        logger.info("atGatewaySessionUpdatePush sid2Uids:[{}] ", push.getSid2Uids().size());
        var sid2Uids = push.getSid2Uids();
        for(var sid2Uid : sid2Uids) {
            pushService.addGatewaySid(session, sid2Uid.getSid(), sid2Uid.getUid());
        }
    }

}
