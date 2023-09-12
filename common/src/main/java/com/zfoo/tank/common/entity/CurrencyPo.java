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

import com.zfoo.tank.common.protocol.common.CurrencyVo;

/**
 * @author jaysunxiao
 */
public class CurrencyPo {

    // 能量
    private int energy;

    // 钻石
    private int gem;

    // 金币
    private int gold;

    public CurrencyVo toCurrencyVO() {
        return CurrencyVo.valueOf(energy, gem, gold);
    }

    public void addEnergy(int addEnergy) {
        this.energy += addEnergy;
    }

    public void addGold(int addGold) {
        this.gold += addGold;
    }

    public void addGem(int addGem) {
        this.gem += addGem;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getGem() {
        return gem;
    }

    public void setGem(int gem) {
        this.gem = gem;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
