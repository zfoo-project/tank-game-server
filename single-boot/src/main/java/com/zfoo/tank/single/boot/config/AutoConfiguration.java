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

package com.zfoo.tank.single.boot.config;

import com.zfoo.net.config.model.NetConfig;
import com.zfoo.orm.model.config.OrmConfig;
import com.zfoo.storage.model.config.StorageConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author jaysunxiao
 * @version 3.0
 */
@Configuration
public class AutoConfiguration {

    @Bean
    public StorageConfig storageConfig(Environment environment) {
        var storageConfig = new StorageConfig();
        storageConfig.setId(environment.getProperty("storage.storageManager"));
        storageConfig.setScanPackage(environment.getProperty("storage.scan.package"));
        storageConfig.setResourceLocation(environment.getProperty("storage.resource.location"));
        return storageConfig;
    }


    @Bean
    @ConfigurationProperties(prefix = "orm")
    public OrmConfig ormConfig() {
        return new OrmConfig();
    }


    @Bean
    @ConfigurationProperties(prefix = "net")
    public NetConfig netConfig() {
        return new NetConfig();
    }
}
