package com.zfoo.tank.room.controller;

import com.zfoo.net.anno.PacketReceiver;
import com.zfoo.net.session.Session;
import com.zfoo.orm.anno.EntityCacheAutowired;
import com.zfoo.orm.cache.IEntityCache;
import com.zfoo.tank.common.entity.RoomEntity;
import com.zfoo.tank.common.protocol.transfer.AdminToRoomTransfer;
import com.zfoo.tank.common.protocol.transfer.HomeToRoomTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransferRoomController {
    private static final Logger logger = LoggerFactory.getLogger(TransferRoomController.class);
    @EntityCacheAutowired
    private IEntityCache<Long, RoomEntity> roomEntities;

    @PacketReceiver
    public void atHomeToRoomTransfer(Session session, HomeToRoomTransfer transfer) {
        var uid = transfer.getUid();
        var roomId = transfer.getRoomId();

        var roomEntity = roomEntities.load(roomId);

        logger.info("atHomeToRoomTransfer uid:[{}] roomId:[{}]", uid, roomId);
        if (transfer.getCreateRoomTransfer() != null) {
            var roomServerAddress = transfer.getCreateRoomTransfer().getRoomServerAddress();
            logger.info("create room uid:[{}] roomId:[{}] roomServerAddress:[{}]", uid, roomId, roomServerAddress);
            // do something when create room
            return;
        }
    }


    @PacketReceiver
    public void atAdminToRoomTransfer(Session session, AdminToRoomTransfer transfer) {
        var roomId = transfer.getRoomId();

        var roomEntity = roomEntities.load(roomId);

        logger.info("atAdminToRoomTransfer roomId:[{}]", roomId);
        if (transfer.getCloseRoomTransfer() != null) {
            // do something when close room
            return;
        }
    }
}
