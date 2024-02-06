package com.zfoo.tank.gateway.controller;

import com.zfoo.net.anno.PacketReceiver;
import com.zfoo.net.router.attachment.SignalAttachment;
import com.zfoo.net.session.Session;
import com.zfoo.tank.common.protocol.transfer.AdminToRoomTransfer;
import com.zfoo.tank.common.protocol.transfer.HomeToRoomTransfer;
import com.zfoo.tank.common.protocol.transfer.RoomToHomeTransfer;
import com.zfoo.tank.gateway.server.MyGatewayRouteHandler;
import com.zfoo.tank.gateway.service.GatewayRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TransferController {

    private static final Logger logger = LoggerFactory.getLogger(PushGatewayController.class);

    @Autowired
    private GatewayRoomService gatewayRoomService;

    @PacketReceiver
    public void atRoomToHomeTransfer(Session session, RoomToHomeTransfer transfer) {
        logger.info("atRoomToHomeTransfer roomId:[{}] uid:[{}]", transfer.getRoomId(), transfer.getUid());
        var uid = transfer.getUid();
        var signalAttachment = new SignalAttachment();
        signalAttachment.setTaskExecutorHash((int) uid);
        MyGatewayRouteHandler.forwardingPacket(transfer, transfer, uid);
    }

    @PacketReceiver
    public void atHomeToRoomTransfer(Session session, HomeToRoomTransfer transfer) {
        if (transfer.getCreateRoomTransfer() != null) {
            logger.info("Home2RoomCreateRoomTransfer uid:[{}] roomId:[{}] roomServerAddress:[{}]", transfer.getUid(), transfer.getRoomId(), transfer.getCreateRoomTransfer().getRoomServerAddress());
            gatewayRoomService.addRoomServerAddress(transfer.getRoomId(), transfer.getCreateRoomTransfer().getRoomServerAddress());
        }
        logger.info("atHomeToRoomTransfer roomId:[{}] uid:[{}]", transfer.getRoomId(), transfer.getUid());
        gatewayRoomService.forwardingPacketToRoom(transfer, transfer.getRoomId());
    }

    @PacketReceiver
    public void atAdminToRoomTransfer(Session session, AdminToRoomTransfer transfer) {
        logger.info("atAdminToRoomTransfer roomId:[{}]", transfer.getRoomId());
        gatewayRoomService.forwardingPacketToRoom(transfer, transfer.getRoomId());
    }
}
