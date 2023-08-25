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
import com.zfoo.tank.common.protocol.common.RankInfo;

import java.util.List;

/**
 * @author jaysunxiao
 * @version 3.0
 */
public class ScoreRankResponse implements IPacket {

    // 排名，按list的先后顺序
    private List<RankInfo> ranks;

    public static ScoreRankResponse valueOf(List<RankInfo> ranks) {
        var packet = new ScoreRankResponse();
        packet.ranks = ranks;
        return packet;
    }

    public List<RankInfo> getRanks() {
        return ranks;
    }

    public void setRanks(List<RankInfo> ranks) {
        this.ranks = ranks;
    }
}
