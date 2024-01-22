package com.zfoo.tank.home.service;

import com.zfoo.net.router.attachment.GatewayAttachment;
import com.zfoo.net.session.Session;
import com.zfoo.orm.anno.EntityCacheAutowired;
import com.zfoo.orm.cache.IEntityCache;
import com.zfoo.tank.common.entity.PlayerEntity;
import org.springframework.stereotype.Component;

/**
 * @author godotg
 */
@Component
public class PlayerService {

    @EntityCacheAutowired
    private IEntityCache<Long, PlayerEntity> playerEntities;

    public PlayerEntity loadPlayer(Session session, GatewayAttachment attachment) {
        var consumerSid = session.getSid();
        var gsid = attachment.getSid();
        var uid = attachment.getUid();

        // 每次请求都更新一下网关信息
        var playerEntity = playerEntities.load(uid);
        playerEntity.getGsid().setConsumerSid(consumerSid);
        playerEntity.getGsid().setGatewaySid(gsid);

        return playerEntity;
    }

}
