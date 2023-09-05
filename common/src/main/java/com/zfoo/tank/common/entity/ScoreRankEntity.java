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

package com.zfoo.tank.common.entity;

import com.zfoo.orm.anno.EntityCache;
import com.zfoo.orm.anno.GraalvmNativeEntityCache;
import com.zfoo.orm.anno.Id;
import com.zfoo.orm.anno.Index;
import com.zfoo.orm.model.IEntity;

/**
 * @author jaysunxiao
 * @version 3.0
 */
@EntityCache
@GraalvmNativeEntityCache
public class ScoreRankEntity implements IEntity<Long> {

    @Id
    private long id;

    // 玩家id
    @Index(ascending = false, unique = false)
    private long playerId;

    private long time;

    // 对应的分数
    @Index(ascending = true, unique = false)
    private int score;

    public static ScoreRankEntity valueOf(long id, long playerId, long time, int score) {
        var entity = new ScoreRankEntity();
        entity.id = id;
        entity.playerId = playerId;
        entity.time = time;
        entity.score = score;
        return entity;
    }

    @Override
    public Long id() {
        return id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
