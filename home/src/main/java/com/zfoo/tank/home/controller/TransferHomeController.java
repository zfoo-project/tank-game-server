package com.zfoo.tank.home.controller;

import com.zfoo.net.anno.PacketReceiver;
import com.zfoo.net.session.Session;
import com.zfoo.orm.anno.EntityCacheAutowired;
import com.zfoo.orm.cache.IEntityCache;
import com.zfoo.tank.common.entity.PlayerEntity;
import com.zfoo.tank.common.protocol.transfer.RoomToHomeTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class TransferHomeController {
    private static final Logger logger = LoggerFactory.getLogger(TransferHomeController.class);
    @EntityCacheAutowired
    private IEntityCache<Long, PlayerEntity> playerEntities;

    @PacketReceiver
    public void atRoomToHomeTransfer(Session session, RoomToHomeTransfer transfer) {
        var uid = transfer.getUid();
        var roomId = transfer.getRoomId();

        logger.info("atRoomToHomeTransfer uid:[{}] roomId:[{}]", uid, roomId);
        var player = playerEntities.load(uid);
        if (transfer.getJoinRoomTransfer() != null) {
            logger.info("JoinRoomTransfer uid:[{}] roomId:[{}]", uid, roomId);
            player.setJoinedRoomId(roomId);
            playerEntities.update(player);
        }

    }

}
