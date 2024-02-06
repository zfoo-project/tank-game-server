package com.zfoo.tank.home.controller;

import com.zfoo.net.NetContext;
import com.zfoo.net.anno.PacketReceiver;
import com.zfoo.net.consumer.registry.Register;
import com.zfoo.net.router.attachment.GatewayAttachment;
import com.zfoo.net.session.Session;
import com.zfoo.orm.OrmContext;
import com.zfoo.orm.anno.EntityCacheAutowired;
import com.zfoo.orm.cache.IEntityCache;
import com.zfoo.orm.util.MongoIdUtils;
import com.zfoo.protocol.util.RandomUtils;
import com.zfoo.tank.common.constant.ProviderEnum;
import com.zfoo.tank.common.entity.PlayerEntity;
import com.zfoo.tank.common.entity.RoomEntity;
import com.zfoo.tank.common.protocol.room.CreateRoomRequest;
import com.zfoo.tank.common.protocol.room.CreateRoomResponse;
import com.zfoo.tank.common.protocol.transfer.Home2RoomCreateRoomTransfer;
import com.zfoo.tank.common.protocol.transfer.HomeToRoomTransfer;
import com.zfoo.tank.common.result.CodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


@Component
public class RoomHomeController {
    private static final Logger logger = LoggerFactory.getLogger(RoomHomeController.class);

    @EntityCacheAutowired
    private IEntityCache<Long, PlayerEntity> playerEntities;


    @PacketReceiver
    public void atCreateRoomRequest(Session session, CreateRoomRequest request, GatewayAttachment attachment) {
        var uid = attachment.getUid();
        var sid = attachment.getSid();

        var title = request.title();
        var subTitle = request.subTitle();
        var description = request.description();
        var visible = request.visible();
        var password = request.password();
        var icon = request.icon();
        var gameId = request.gameId();
        var maxNums = request.maxNums();

        logger.info("atCreateRoomRequest uid:[{}][{}] title:[{}] visible:[{}] password:[{}] gameId:[{}] maxNums:[{}]", uid, sid, title, visible, password, gameId, maxNums);

        if (maxNums < 0 || maxNums >= 12) {
            NetContext.getRouter().send(session, CodeEnum.error(CodeEnum.room_player_range_limit));
            return;
        }

        var player = playerEntities.load(uid);

        // -------------------------------------------------------------------------------------------------------------
        // 分配房间的服务器，从zk查询当前可以提供房间服务的服务器
        var allProviders = NetContext.getConfigManager().getRegistry().remoteProviderRegisters();
        if (CollectionUtils.isEmpty(allProviders)) {
            NetContext.getRouter().send(session, CodeEnum.error(CodeEnum.room_server_limit));
            return;
        }
        var roomProviders = new ArrayList<Register>();
        for (var register : allProviders) {
            var providerConfig = register.getProviderConfig();
            if (providerConfig == null) {
                continue;
            }
            if (providerConfig.getProviders().stream().noneMatch(it -> it.getProvider().equals(ProviderEnum.tankRoomProvider.name()))) {
                continue;
            }
            roomProviders.add(register);
        }
        if (CollectionUtils.isEmpty(roomProviders)) {
            NetContext.getRouter().send(session, CodeEnum.error(CodeEnum.room_no_resource));
            return;
        }
        // 随机选择一个房间服务器
        var roomProvider = RandomUtils.randomEle(roomProviders);
        var roomServerAddress = roomProvider.getProviderConfig().getAddress();

        // -------------------------------------------------------------------------------------------------------------
        // 创建房间的消耗


        // -------------------------------------------------------------------------------------------------------------
        // 谁创建的谁就是房主
        var roomId = MongoIdUtils.getIncrementIdFromMongoDefault(RoomEntity.class);
        var roomEntity = new RoomEntity(roomId, uid, List.of(player.toPlayerInfo())
                , title, subTitle, description, visible, password, icon, gameId, maxNums, roomServerAddress);
        OrmContext.getAccessor().insert(roomEntity);

        // 转发给room server
        var transfer = new HomeToRoomTransfer(roomId, uid);
        transfer.setCreateRoomTransfer(new Home2RoomCreateRoomTransfer(roomServerAddress));
        NetContext.getRouter().send(session, transfer, null);

        NetContext.getRouter().send(session, new CreateRoomResponse(roomEntity.toRoom()), attachment);
    }

}
