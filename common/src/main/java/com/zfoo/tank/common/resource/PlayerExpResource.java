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

package com.zfoo.tank.common.resource;

import com.zfoo.storage.model.anno.Id;
import com.zfoo.storage.model.anno.Resource;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2021-01-20 16:20
 */
@Resource
public class PlayerExpResource {

    @Id
    private int playerLevel;

    private int exp;

    private int gem;

    private int gold;

    public int getPlayerLevel() {
        return playerLevel;
    }

    public int getExp() {
        return exp;
    }

    public int getGem() {
        return gem;
    }

    public int getGold() {
        return gold;
    }
}
