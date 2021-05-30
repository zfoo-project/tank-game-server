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

package com.zfoo.tank.common.protocol.cache;

import com.zfoo.protocol.IPacket;

/**
 * @author jaysunxiao
 * @version 3.0
 */
public class BattleScoreAnswer implements IPacket {

    public static final transient short PROTOCOL_ID = 3001;

    // 如果上榜则给奖励
    private boolean rankReward;

    public static BattleScoreAnswer valueOf(boolean rankReward) {
        var packet = new BattleScoreAnswer();
        packet.rankReward = rankReward;
        return packet;
    }

    @Override
    public short protocolId() {
        return PROTOCOL_ID;
    }

    public boolean isRankReward() {
        return rankReward;
    }

    public void setRankReward(boolean rankReward) {
        this.rankReward = rankReward;
    }
}
