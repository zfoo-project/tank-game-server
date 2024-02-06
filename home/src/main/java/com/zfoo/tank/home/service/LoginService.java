package com.zfoo.tank.home.service;

import com.zfoo.net.NetContext;
import com.zfoo.net.router.attachment.GatewayAttachment;
import com.zfoo.net.session.Session;
import com.zfoo.orm.anno.EntityCacheAutowired;
import com.zfoo.orm.cache.IEntityCache;
import com.zfoo.scheduler.util.TimeUtils;
import com.zfoo.tank.common.entity.PlayerEntity;
import com.zfoo.tank.common.protocol.login.KickPlayerAsk;
import org.springframework.stereotype.Component;

@Component
public class LoginService {

    @EntityCacheAutowired
    private IEntityCache<Long, PlayerEntity> playerEntities;


    public void login(PlayerEntity player, Session session, GatewayAttachment attachment) {
        // 释放之前的tcp连接
        var gateway = player.getGsid();
        var oldSession = NetContext.getSessionManager().getServerSession(gateway.getConsumerSid());
        if (oldSession != null) {
            NetContext.getRouter().send(oldSession, new KickPlayerAsk(gateway.getGatewaySid(), player.getId()), null);
        }
        // 更新现在的路由信息
        gateway.update(session.getSid(), attachment.getSid());
        playerEntities.update(player);
    }


    public void logout(PlayerEntity player, Session session) {
        player.setLastLogoutTime(TimeUtils.now());
        player.getGsid().clear();

        playerEntities.updateNow(player);
    }


}
