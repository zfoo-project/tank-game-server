/*
 * Copyright (C) 2020 The zfoo Authors
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.tank.gateway.client;

import com.zfoo.net.anno.PacketReceiver;
import com.zfoo.net.session.Session;
import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.tank.common.protocol.CurrencyUpdateNotice;
import com.zfoo.tank.common.protocol.PlayerExpNotice;
import com.zfoo.tank.common.protocol.battle.BattleResultResponse;
import com.zfoo.tank.common.protocol.cache.ScoreRankResponse;
import com.zfoo.tank.common.protocol.login.GetPlayerInfoResponse;
import com.zfoo.tank.common.protocol.login.LoginByHttpTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author jaysunxiao
 */
@Component
public class MyTankClientController {

    private static final Logger logger = LoggerFactory.getLogger(MyTankClientController.class);


    @PacketReceiver
    public void atLoginByHttpTokenResponse(Session session, LoginByHttpTokenResponse response) {
        logger.info("客户端收到服务器的登录返回[{}]", JsonUtils.object2String(response));
    }


    @PacketReceiver
    public void atGetPlayerInfoResponse(Session session, GetPlayerInfoResponse response) {
        var playerInfo = response.getPlayerInfo();
        var currencyVo = response.getCurrencyVo();

        logger.info("玩家基础信息[id:{}][name:{}]", playerInfo.getId(), playerInfo.getName());

        MyTankData.playerInfo = playerInfo;
        MyTankData.currencyVo = currencyVo;
    }


    @PacketReceiver
    public void atBattleResultResponse(Session session, BattleResultResponse response) {
        logger.info("战斗结果[{}]", JsonUtils.object2String(response));
    }

    @PacketReceiver
    public void atScoreRankResponse(Session session, ScoreRankResponse response) {
        logger.info("排行榜信息[{}]", JsonUtils.object2String(response));
    }

    @PacketReceiver
    public void atCurrencyUpdateNotice(Session session, CurrencyUpdateNotice notice) {
        logger.info("currency update [{}]", JsonUtils.object2String(notice));
    }

    @PacketReceiver
    public void atPlayerExpNotice(Session session, PlayerExpNotice response) {
        logger.info("player exp update [{}]", JsonUtils.object2String(response));
    }


}
