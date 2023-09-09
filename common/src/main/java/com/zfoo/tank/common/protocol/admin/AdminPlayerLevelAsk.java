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

package com.zfoo.tank.common.protocol.admin;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2019-12-05 10:43
 */
public class AdminPlayerLevelAsk {

    private long userId;
    private int playerLevel;
    private int playerExp;

    public static AdminPlayerLevelAsk valueOf(long userId, int playerLevel, int playerExp) {
        var ask = new AdminPlayerLevelAsk();
        ask.userId = userId;
        ask.playerLevel = playerLevel;
        ask.playerExp = playerExp;
        return ask;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public int getPlayerExp() {
        return playerExp;
    }

    public void setPlayerExp(int playerExp) {
        this.playerExp = playerExp;
    }
}
