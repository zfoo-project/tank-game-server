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

package com.zfoo.tank.admin.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zfoo.orm.anno.EntityCache;
import com.zfoo.orm.anno.Id;
import com.zfoo.orm.model.IEntity;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2019-12-03 10:46
 */
@EntityCache
public class AdminEntity implements IEntity<String> {

    @Id
    private String id;

    @JsonIgnore
    private String password;

    private String name;

    private String avatar;

    private SettingPO setting = new SettingPO();

    public static AdminEntity valueOf(String id, String password) {
        var entity = new AdminEntity();
        entity.id = id;
        entity.password = password;
        return entity;
    }

    @Override
    public String id() {
        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public SettingPO getSetting() {
        return setting;
    }

    public void setSetting(SettingPO setting) {
        this.setting = setting;
    }
}
