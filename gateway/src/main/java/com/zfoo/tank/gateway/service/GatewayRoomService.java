package com.zfoo.tank.gateway.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zfoo.net.NetContext;
import com.zfoo.net.router.attachment.GatewayAttachment;
import com.zfoo.net.router.attachment.SignalAttachment;
import com.zfoo.net.session.Session;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.scheduler.util.TimeUtils;
import com.zfoo.tank.common.protocol.room.JoinRoomRequest;
import com.zfoo.tank.common.protocol.room.RoomInfoAnswer;
import com.zfoo.tank.common.protocol.room.RoomInfoAsk;
import com.zfoo.tank.gateway.controller.GatewayController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class GatewayRoomService {
    private static final Logger logger = LoggerFactory.getLogger(GatewayRoomService.class);
    // key->roomId, value->roomServerAddress
    private Cache<Long, String> roomServerAddressCaches = Caffeine.newBuilder()
            .expireAfterAccess(2 * TimeUtils.MILLIS_PER_HOUR, TimeUnit.MILLISECONDS)
            .maximumSize(10000).build();

    public void addRoomServerAddress(long roomId, String roomServerAddress) {
        roomServerAddressCaches.put(roomId, roomServerAddress);
    }

    /**
     * 转发网关收到的包到Room
     */
    @Nullable
    public Session selectRoomServerSession(long roomId) {
        var roomServerAddress = roomServerAddressCaches.getIfPresent(roomId);
        if (StringUtils.isBlank(roomServerAddress)) {
            var ask = new RoomInfoAsk(roomId);
            RoomInfoAnswer answer = null;
            try {
                answer = NetContext.getConsumer().syncAsk(ask, RoomInfoAnswer.class, roomId).packet();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            roomServerAddress = answer.getRoomServerAddress();
            addRoomServerAddress(roomId, roomServerAddress);
        }
        // 网关统一用 moduleid uid 获取 session
        var providers = NetContext.getConsumer().findProviders(new JoinRoomRequest(roomId));
        for(var provider : providers) {
            var providerConfig = provider.getConsumerRegister().getProviderConfig();
            if (providerConfig.getAddress().equals(roomServerAddress)) {
                return provider;
            }
        }
        return null;
    }

    public void forwardingPacketToRoom(Object packet, GatewayAttachment attachment, long roomId) {
        attachment.setTaskExecutorHash((int) roomId);
        var roomSession = selectRoomServerSession(roomId);
        if (roomSession == null) {
            logger.error("forwardingPacketToRoom GatewayAttachment not found roomId:[{}]", roomId);
            return;
        }
        NetContext.getRouter().send(roomSession, packet, attachment);
    }
    public void forwardingPacketToRoom(Object packet, long roomId) {
        var signalAttachment = new SignalAttachment();
        signalAttachment.setTaskExecutorHash((int) roomId);
        var roomSession = selectRoomServerSession(roomId);
        if (roomSession == null) {
            logger.error("forwardingPacketToRoom SignalAttachment not found roomId:[{}]", roomId);
            return;
        }
        NetContext.getRouter().send(roomSession, packet, signalAttachment);
    }


    public boolean filterRoomPacket(Object packet, GatewayAttachment attachment) {
        var packetClass = packet.getClass();
        if (packetClass == JoinRoomRequest.class) {
            forwardingPacketToRoom(packet, attachment, ((JoinRoomRequest) packet).roomId());
            return true;
        }
        return false;
    }

}
