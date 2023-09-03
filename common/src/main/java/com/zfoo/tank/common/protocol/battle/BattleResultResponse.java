/*
 *  Copyright (C) 2020 The zfoo Authors
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.tank.common.protocol.battle;

import com.zfoo.net.packet.IPacket;

/**
 * @author jaysunxiao
 * @version 3.0
 */
public class BattleResultResponse implements IPacket {

    private int score;

    public static BattleResultResponse valueOf(int score) {
        var packet = new BattleResultResponse();
        packet.score = score;
        return packet;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
