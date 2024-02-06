package com.zfoo.tank.room.controller;

import com.zfoo.net.NetContext;
import com.zfoo.net.anno.PacketReceiver;
import com.zfoo.net.router.attachment.GatewayAttachment;
import com.zfoo.net.session.Session;
import com.zfoo.orm.OrmContext;
import com.zfoo.orm.anno.EntityCacheAutowired;
import com.zfoo.orm.cache.IEntityCache;
import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.tank.common.entity.PlayerEntity;
import com.zfoo.tank.common.entity.RoomEntity;
import com.zfoo.tank.common.protocol.push.CommonNoticePush;
import com.zfoo.tank.common.protocol.room.*;
import com.zfoo.tank.common.protocol.transfer.Room2HomeJoinTransfer;
import com.zfoo.tank.common.protocol.transfer.RoomToHomeTransfer;
import com.zfoo.tank.common.result.CodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
    @EntityCacheAutowired
    private IEntityCache<Long, RoomEntity> roomEntities;


    @PacketReceiver
    public void atJoinRoomRequest(Session session, JoinRoomRequest request, GatewayAttachment attachment) {
        var uid = attachment.getUid();
        var sid = attachment.getSid();

        var roomId = request.roomId();

        logger.info("atJoinRoomRequest uid:[{}][{}] roomId:[{}]", uid, sid, roomId);

        var roomEntity = roomEntities.load(roomId);
        var currentUserIds = roomEntity.getPlayers();
        if (currentUserIds.size() >= roomEntity.getMaxNums()) {
            NetContext.getRouter().send(session, CodeEnum.error(CodeEnum.room_max_num_limit), attachment);
            return;
        }

        // 房间里没有该玩家则添加数据
        var matchPlayer = roomEntity.matchPlayerById(uid);
        if (matchPlayer == null) {
            var player = OrmContext.getAccessor().load(uid, PlayerEntity.class);
            matchPlayer = player.toPlayerInfo();
            roomEntity.getPlayers().add(matchPlayer);
        }

        if (roomEntity.getOwner() == 0) {
            roomEntity.setOwner(uid);
        }


        roomEntities.update(roomEntity);

        var response = new JoinRoomResponse(roomEntity.toRoom());
        NetContext.getRouter().send(session, response, attachment);

        // 转发给lobby，记录一下加入的房间，方便断线重连
        var transfer = new RoomToHomeTransfer(roomId, uid);
        transfer.setJoinRoomTransfer(new Room2HomeJoinTransfer());
        NetContext.getRouter().send(session, transfer, null);

        var targets = roomEntity.toPlayerIds();
        var notice = new JoinRoomNotice(roomEntity.toRoom(), roomEntity.getPlayers());
        var push = new CommonNoticePush(targets);
        push.setJoinRoomNotice(notice);
        NetContext.getConsumer().send(push, roomId);
    }

    @PacketReceiver
    public void atExitRoomRequest(Session session, ExitRoomRequest request, GatewayAttachment attachment) {
        var uid = attachment.getUid();
        var sid = attachment.getSid();

        var roomId = request.roomId();

        logger.info("atExitRoomRequest uid:[{}][{}] roomId:[{}]", uid, sid, roomId);

        var roomEntity = roomEntities.load(roomId);
        if (roomEntity.getPlayers().stream().noneMatch(it -> it.getId() == uid)) {
            NetContext.getRouter().send(session, CodeEnum.error(CodeEnum.room_no_player), attachment);
            return;
        }

        roomEntity.getPlayers().removeIf(it -> it.getId() == uid);

        // 房间没人则销毁房间和ds
        if (CollectionUtils.isEmpty(roomEntity.getPlayers())) {
            logger.info("atExitRoomRequest no players and close roomId:[{}]", roomId);
            roomEntities.update(roomEntity);
            NetContext.getRouter().send(session, new ExitRoomResponse(), attachment);
            return;
        }


        roomEntities.update(roomEntity);

        NetContext.getRouter().send(session, new ExitRoomResponse(), attachment);

        var targets = roomEntity.toPlayerIds();
        var notice = new ExitRoomNotice(roomEntity.toRoom(), roomEntity.getPlayers());
        var push = new CommonNoticePush(targets);
        push.setExitRoomNotice(notice);
        NetContext.getConsumer().send(push, roomId);
    }


    @PacketReceiver
    public void atRoomInfoAsk(Session session, RoomInfoAsk ask) {
        var roomId = ask.getRoomId();
        logger.info("atRoomInfoAsk roomId:[{}]", roomId);
        var roomEntity = roomEntities.load(roomId);
        var room = roomEntity.toRoom();
        var answer = new RoomInfoAnswer(room, roomEntity.getRoomServerAddress());
        NetContext.getRouter().send(session, answer);
    }

}
