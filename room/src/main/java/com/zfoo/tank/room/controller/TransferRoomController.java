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
    public void atLobbyToRoomTransfer(Session session, HomeToRoomTransfer transfer) {
        var uid = transfer.getUid();
        var roomId = transfer.getRoomId();

        var roomEntity = roomEntities.load(roomId);

        logger.info("atLobbyToRoomTransfer uid:[{}] roomId:[{}]", uid, roomId);
        if (transfer.getCreateRoomTransfer() != null) {
            var roomServerAddress = transfer.getCreateRoomTransfer().getRoomServerAddress();
            logger.info("start dedicated server uid:[{}] roomId:[{}] roomServerAddress:[{}]", uid, roomId, roomServerAddress);

            if (roomEntity.empty()) {
                logger.error("start dedicated server not found roomId:[{}] not found", roomId);
                return;
            }
            if (!roomServerAddress.equals(roomEntity.getRoomServerAddress())) {
                logger.error("start dedicated roomId:[{}] roomServerAddress:[{}] not equal to roomServer:[{}]", roomId, roomServerAddress, roomEntity.getRoomServerAddress());
                return;
            }
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
