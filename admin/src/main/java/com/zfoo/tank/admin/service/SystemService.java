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

package com.zfoo.tank.admin.service;

import com.zfoo.event.model.event.AppStartEvent;
import com.zfoo.orm.OrmContext;
import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.tank.admin.model.entity.AdminEntity;
import com.zfoo.tank.common.util.HotUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2021-03-12 14:01
 */
@Component
public class SystemService implements ApplicationListener<AppStartEvent> {


    public void onApplicationEvent(AppStartEvent appStartEvent) {
        // 热更新
        HotUtils.startHotSwapConfig();
        HotUtils.startHotSwapJava();


        // 在数据库初始化一个管理员账号和密码
        var id = "test";
        var password = "test";

        var results = OrmContext.getQuery(AdminEntity.class).eq("_id", id).queryAll();
        if (CollectionUtils.isEmpty(results)) {
            OrmContext.getAccessor().insert(AdminEntity.valueOf(id, password));
        }
    }


}
