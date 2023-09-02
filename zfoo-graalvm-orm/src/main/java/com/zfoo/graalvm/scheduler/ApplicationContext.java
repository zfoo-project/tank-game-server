/*
 * Copyright (C) 2020 The zfoo Authors
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.graalvm.scheduler;

import com.zfoo.orm.OrmContext;
import com.zfoo.orm.cache.IEntityCaches;
import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.protocol.util.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    public static void main(String[] args) {
        var context = SpringApplication.run(ApplicationContext.class, args);

        var id = RandomUtils.randomLong();
        var userEntity = new UserEntity(id, (byte) 2, (short) 3, 5, true, "orm", "orm");
        OrmContext.getAccessor().insert(userEntity);
        logger.info("insert [{}] {}", id, userEntity);

        var loadEntity = OrmContext.getAccessor().load(id, UserEntity.class);
        logger.info("load [{}] {}", id, JsonUtils.object2String(loadEntity));

        userEntity.setE("update");
        userEntity.setF("update udpate");
        OrmContext.getAccessor().update(userEntity);
        logger.info("update [{}] {}", id, JsonUtils.object2String(OrmContext.getAccessor().load(id, UserEntity.class)));

        OrmContext.getAccessor().delete(id, UserEntity.class);
        logger.info("delete [{}] {}", id, OrmContext.getAccessor().load(id, UserEntity.class));

        // 通过注解自动注入的方式去拿到UserEntity的EntityCaches
        var userEntityCaches = context.getBean(UserManager.class).userEntityCaches;
        var userEntityCaches2 = (IEntityCaches<Long, UserEntity>) OrmContext.getOrmManager().getEntityCaches(UserEntity.class);
        logger.info("entity cache [{}] [{}]", userEntityCaches, userEntityCaches2);
        OrmContext.getAccessor().insert(loadEntity);
        for (int i = 1; i <= 10; i++) {
            var entity = userEntityCaches.load(id);
            entity.setE("update" + i);
            entity.setC(i);
            userEntityCaches.update(entity);
        }
    }



}
