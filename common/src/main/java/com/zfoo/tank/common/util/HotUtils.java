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

package com.zfoo.tank.common.util;

import com.zfoo.hotswap.util.HotSwapUtils;
import com.zfoo.net.NetContext;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.storage.StorageContext;
import com.zfoo.storage.model.vo.Storage;
import com.zfoo.tank.common.constant.GameConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author yidingzhao
 * @version 1.0
 * @since 2021-02-05 13:42
 */
public abstract class HotUtils {

    private static final Logger logger = LoggerFactory.getLogger(HotUtils.class);

    public static Map<String, Class<?>> configSimpleClazzNameMap() {
        Map<String, Class<?>> clazzSimpleNameMap = StorageContext.getStorageManager().allStorageClassSet()
                .stream()
                .collect(Collectors.toMap(key -> key.getSimpleName(), value -> value));
        return clazzSimpleNameMap;
    }

    public static void startHotSwapConfig() {
        logger.info("开启Excel配置表热更新");
        NetContext.getConfigManager().getRegistry().addListener(GameConstant.EXCEL_HOTSWAP_ZK_PATH, new BiConsumer<String, byte[]>() {
            @Override
            public void accept(String path, byte[] bytes) {
                if (path.equals(GameConstant.EXCEL_HOTSWAP_ZK_PATH)) {
                    return;
                }

                if (bytes == null) {
                    logger.info("zk收到path[{}]更新length[{}]", path, 0);
                    return;
                }

                logger.info("zk收到path[{}]更新length[{}]", path, bytes.length);

                var configName = StringUtils.substringAfterLast(path, StringUtils.SLASH);
                var clazzSimpleNameMap = HotUtils.configSimpleClazzNameMap();
                var clazz = clazzSimpleNameMap.get(configName);
                Storage<?, ?> storage = new Storage<>();
                storage.init(new ByteArrayInputStream(bytes), clazz);
                StorageContext.getStorageManager().updateStorage(clazz, storage);
                StorageContext.getStorageManager().inject();
            }
        }, null);
    }


    public static void startHotSwapJava() {
        logger.info("开启Java文件热更新");
        NetContext.getConfigManager().getRegistry().addListener(GameConstant.JAVA_HOTSWAP_ZK_PATH, new BiConsumer<String, byte[]>() {
            @Override
            public void accept(String path, byte[] bytes) {
                if (path.equals(GameConstant.JAVA_HOTSWAP_ZK_PATH)) {
                    return;
                }

                if (bytes == null) {
                    logger.info("zk收到path[{}]更新length[{}]", path, 0);
                    return;
                }

                logger.info("zk收到path[{}]更新length[{}]", path, bytes.length);

                HotSwapUtils.hotswapClass(bytes);
            }
        }, null);
    }

}
