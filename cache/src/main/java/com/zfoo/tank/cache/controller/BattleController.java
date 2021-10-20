/*
 * Copyright (C) 2020 The zfoo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.tank.cache.controller;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.zfoo.event.model.event.AppStartEvent;
import com.zfoo.net.NetContext;
import com.zfoo.net.router.attachment.GatewayAttachment;
import com.zfoo.net.router.receiver.PacketReceiver;
import com.zfoo.net.session.model.Session;
import com.zfoo.net.util.SingleCache;
import com.zfoo.orm.OrmContext;
import com.zfoo.orm.util.MongoIdUtils;
import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.scheduler.util.TimeUtils;
import com.zfoo.tank.common.entity.PlayerEntity;
import com.zfoo.tank.common.entity.ScoreRankEntity;
import com.zfoo.tank.common.protocol.cache.BattleScoreAnswer;
import com.zfoo.tank.common.protocol.cache.BattleScoreAsk;
import com.zfoo.tank.common.protocol.cache.ScoreRankRequest;
import com.zfoo.tank.common.protocol.cache.ScoreRankResponse;
import com.zfoo.tank.common.protocol.common.PlayerInfo;
import com.zfoo.tank.common.protocol.common.RankInfo;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jaysunxiao
 * @version 3.0
 */
@Component
public class BattleController implements ApplicationListener<AppStartEvent> {

    private static final int RANK_SIZE = 20;

    private volatile int rankLimit = 0;
    private volatile int minScore = 0;

    private SingleCache<List<RankInfo>> rankCache;


    @Override
    public void onApplicationEvent(AppStartEvent appStartEvent) {
        rankCache = SingleCache.build(10 * TimeUtils.MILLIS_PER_SECOND, () -> {
            var rankList = new ArrayList<ScoreRankEntity>();
            OrmContext.getOrmManager()
                    .getCollection(ScoreRankEntity.class)
                    .find()
                    .sort(Sorts.descending("score"))
                    .limit(RANK_SIZE)
                    .forEach(it -> rankList.add(it));

            var playerInfoMap = new HashMap<Long, PlayerInfo>();
            OrmContext.getOrmManager()
                    .getCollection(PlayerEntity.class)
                    .find(Filters.in("_id", rankList.stream().map(it -> it.getPlayerId()).collect(Collectors.toList())))
                    .forEach(it -> playerInfoMap.put(it.getId(), it.toPlayerInfo()));

            var list = new ArrayList<RankInfo>();
            for (var rankEntity : rankList) {
                list.add(RankInfo.valueOf(playerInfoMap.get(rankEntity.getPlayerId()), rankEntity.getTime(), rankEntity.getScore()));
            }

            if (CollectionUtils.isNotEmpty(list)) {
                minScore = list.get(list.size() - 1).getScore();
            }
            rankLimit = list.size();
            return list;
        });
    }

    @PacketReceiver
    public void atScoreRankRequest(Session session, ScoreRankRequest request, GatewayAttachment gatewayAttachment) {
        NetContext.getRouter().send(session, ScoreRankResponse.valueOf(rankCache.get()), gatewayAttachment);
    }

    @PacketReceiver
    public void atBattleScoreAsk(Session session, BattleScoreAsk ask) {
        var playerId = ask.getPlayerId();
        var score = ask.getScore();

        // 如果没有上榜，直接返回；上榜，则将当前成绩插入数据库
        if (score <= minScore && rankLimit >= RANK_SIZE) {
            NetContext.getRouter().send(session, BattleScoreAnswer.valueOf(false));
            return;
        }

        // 获取一个分布式自增唯一id
        var id = MongoIdUtils.getIncrementIdFromMongoDefault(ScoreRankEntity.class);
        // 插入数据库
        OrmContext.getAccessor().insert(ScoreRankEntity.valueOf(id, playerId, TimeUtils.now(), score));
        NetContext.getRouter().send(session, BattleScoreAnswer.valueOf(true));
    }

}
