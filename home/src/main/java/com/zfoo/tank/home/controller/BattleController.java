/*
 *  Copyright (C) 2020 The zfoo Authors
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.tank.home.controller;

import com.zfoo.event.manager.EventBus;
import com.zfoo.net.NetContext;
import com.zfoo.net.router.attachment.GatewayAttachment;
import com.zfoo.net.router.receiver.PacketReceiver;
import com.zfoo.net.session.Session;
import com.zfoo.orm.cache.IEntityCaches;
import com.zfoo.orm.model.anno.EntityCachesInjection;
import com.zfoo.storage.anno.StorageInjection;
import com.zfoo.storage.manager.IStorage;
import com.zfoo.tank.common.entity.PlayerEntity;
import com.zfoo.tank.common.protocol.CurrencyUpdateNotice;
import com.zfoo.tank.common.protocol.PlayerExpNotice;
import com.zfoo.tank.common.protocol.battle.BattleResultRequest;
import com.zfoo.tank.common.protocol.battle.BattleResultResponse;
import com.zfoo.tank.common.protocol.cache.BattleScoreAnswer;
import com.zfoo.tank.common.protocol.cache.BattleScoreAsk;
import com.zfoo.tank.common.resource.PlayerExpResource;
import com.zfoo.tank.home.model.PlayerLevelUpEvent;
import com.zfoo.tank.home.util.SendUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author jaysunxiao
 * @version 3.0
 */
@Component
public class BattleController {

    private static final Logger logger = LoggerFactory.getLogger(BattleController.class);

    @EntityCachesInjection
    private IEntityCaches<Long, PlayerEntity> playerEntityCaches;

    @StorageInjection
    private IStorage<Integer, PlayerExpResource> playerExpStorage;

    @PacketReceiver
    public void atBattleResultRequest(Session session, BattleResultRequest request, GatewayAttachment gatewayAttachment) {
        var uid = gatewayAttachment.getUid();
        var player = playerEntityCaches.load(uid);

        var score = request.getScore();

        // 注意线程号和异步请求回调的线程号是一致的
        logger.info("c[{}][{}]玩家战斗结果[score:{}]", gatewayAttachment.getUid(), gatewayAttachment.getSid(), score);

        // 异步发送给cache服
        NetContext.getConsumer().asyncAsk(BattleScoreAsk.valueOf(uid, score), BattleScoreAnswer.class, uid)
                .whenComplete(answer -> {
                    logger.info("c[{}][{}]玩家战斗结果异步回调", gatewayAttachment.getUid(), gatewayAttachment.getSid());

                    // 战斗过后如果上了排行榜，则奖励一下，每一分值一个金币，半个钻石
                    if (answer.isRankReward()) {
                        var currencyPo = player.getCurrencyPo();
                        currencyPo.setGold(currencyPo.getGold() + score);
                        currencyPo.setGem(currencyPo.getGem() + score / 2);
                        addPlayerExp(player, score);

                        playerEntityCaches.update(player);

                        NetContext.getRouter().send(session, BattleResultResponse.valueOf(score), gatewayAttachment);
                        NetContext.getRouter().send(session, CurrencyUpdateNotice.valueOf(currencyPo.toCurrencyVO()), gatewayAttachment);
                    }
                });
    }

    public void addPlayerExp(PlayerEntity playerEntity, int playerExp) {

        playerEntity.addExp(playerExp);

        for (int i = 0; i < playerExpStorage.size(); i++) {
            var level = playerEntity.getLevel();
            var exp = playerEntity.getExp();

            if (!playerExpStorage.contain(level + 1)) {
                break;
            }

            var playerExpConfig = playerExpStorage.get(level);
            if (exp < playerExpConfig.getExp()) {
                break;
            }
            playerEntity.setLevel(playerEntity.getLevel() + 1);
            playerEntity.setExp(exp - playerExpConfig.getExp());

            // 抛出一个升级的事件
            EventBus.post(PlayerLevelUpEvent.valueOf(playerEntity, level));
        }

        SendUtils.sendToPlayer(playerEntity, PlayerExpNotice.valueOf(playerEntity.getLevel(), playerEntity.getExp()));
    }

}
