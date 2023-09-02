package com.zfoo.tank.home.controller;

import com.zfoo.net.NetContext;
import com.zfoo.net.packet.common.Message;
import com.zfoo.net.router.receiver.PacketReceiver;
import com.zfoo.net.session.Session;
import com.zfoo.orm.anno.EntityCacheAutowired;
import com.zfoo.orm.cache.IEntityCache;
import com.zfoo.tank.common.entity.PlayerEntity;
import com.zfoo.tank.common.protocol.CurrencyUpdateNotice;
import com.zfoo.tank.common.protocol.PlayerExpNotice;
import com.zfoo.tank.common.protocol.admin.AdminCurrencyAsk;
import com.zfoo.tank.common.protocol.admin.AdminPlayerLevelAsk;
import com.zfoo.tank.home.util.SendUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author yidingzhao
 * @version 1.0
 * @since 2021-07-06 18:21
 */
@Component
public class GmController {
    private static final Logger logger = LoggerFactory.getLogger(GmController.class);

    @EntityCacheAutowired
    private IEntityCache<Long, PlayerEntity> playerEntityCaches;

    @PacketReceiver
    public void atAdminPlayerLevelAsk(Session session, AdminPlayerLevelAsk ask) {
        var userId = ask.getUserId();
        var playerLevel = ask.getPlayerLevel();
        var playerExp = ask.getPlayerExp();

        var userEntity = playerEntityCaches.load(userId);
        if (playerLevel > 0) {
            userEntity.setLevel(playerLevel);
            userEntity.setExp(0);

            SendUtils.sendToPlayer(userEntity, PlayerExpNotice.valueOf(userEntity.getLevel(), userEntity.getExp()));
        }
        if (playerExp >= 0) {
            userEntity.setExp(playerExp);
            SendUtils.sendToPlayer(userEntity, PlayerExpNotice.valueOf(userEntity.getLevel(), userEntity.getExp()));
        }

        playerEntityCaches.update(userEntity);

        NetContext.getRouter().send(session, Message.valueOf(ask, 1, null));
    }

    @PacketReceiver
    public void atAdminCurrencyAsk(Session session, AdminCurrencyAsk ask) {
        var userId = ask.getUserId();

        var gold = ask.getGold();
        var gem = ask.getGem();
        var energy = ask.getEnergy();

        var userEntity = playerEntityCaches.load(userId);

        userEntity.getCurrencyPo().setGold(gold);
        userEntity.getCurrencyPo().setGem(gem);
        userEntity.getCurrencyPo().setEnergy(energy);
        playerEntityCaches.update(userEntity);

        SendUtils.sendToPlayer(userEntity, CurrencyUpdateNotice.valueOf(userEntity.getCurrencyPo().toCurrencyVO()));

        NetContext.getRouter().send(session, Message.valueOf(ask, 1, null));
    }
}
