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

package com.zfoo.tank.admin.model;

import com.zfoo.scheduler.util.TimeUtils;
import com.zfoo.tank.common.entity.PlayerEntity;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2020-03-05 11:45
 */
public class PlayerVO {

    private long id;
    private String account;
    private String name;


    private int level;
    private long exp;
    private long prestigeExp;

    private String lastLoginTime;
    private String registerTime;

    public static PlayerVO valueOf(PlayerEntity playerEntity) {
        var userVO = new PlayerVO();
        userVO.id = playerEntity.getId();
        userVO.name = playerEntity.getName();
        userVO.level = playerEntity.getLevel();
        userVO.exp = playerEntity.getExp();
        userVO.lastLoginTime = TimeUtils.timeToString(playerEntity.getLastLoginTime());
        userVO.registerTime = TimeUtils.timeToString(playerEntity.getRegisterTime());

        return userVO;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public long getPrestigeExp() {
        return prestigeExp;
    }

    public void setPrestigeExp(long prestigeExp) {
        this.prestigeExp = prestigeExp;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }
}
