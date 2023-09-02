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

package com.zfoo.graalvm.storage;

import com.zfoo.storage.config.StorageConfig;
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
        storageConfig.setId(environment.getProperty("storage.id"));
        storageConfig.setScanPackage(environment.getProperty("storage.scan.package"));
        storageConfig.setResourceLocation(environment.getProperty("storage.resource.location"));
        return storageConfig;
    }

}
