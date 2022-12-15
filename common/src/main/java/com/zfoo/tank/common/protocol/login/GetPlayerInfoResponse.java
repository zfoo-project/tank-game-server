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

package com.zfoo.tank.common.protocol.login;


import com.zfoo.protocol.IPacket;
import com.zfoo.tank.common.protocol.common.CurrencyVo;
import com.zfoo.tank.common.protocol.common.PlayerInfo;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2019-10-15 17:55
 */
public class GetPlayerInfoResponse implements IPacket {

    private PlayerInfo playerInfo;

    private CurrencyVo currencyVo;

    public static GetPlayerInfoResponse valueOf(PlayerInfo playerInfo, CurrencyVo currencyVo) {
        var packet = new GetPlayerInfoResponse();
        packet.playerInfo = playerInfo;
        packet.currencyVo = currencyVo;
        return packet;
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public CurrencyVo getCurrencyVo() {
        return currencyVo;
    }

    public void setCurrencyVo(CurrencyVo currencyVo) {
        this.currencyVo = currencyVo;
    }
}
