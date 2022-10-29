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

package com.zfoo.tank.single.boot.godot;

import com.zfoo.protocol.ProtocolManager;
import com.zfoo.protocol.buffer.ByteBufUtils;
import com.zfoo.protocol.generate.GenerateOperation;
import com.zfoo.protocol.serializer.CodeLanguage;
import com.zfoo.protocol.util.FileUtils;
import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.storage.manager.StorageManager;
import com.zfoo.storage.model.config.StorageConfig;
import com.zfoo.storage.model.vo.Storage;
import com.zfoo.storage.util.ExportUtils;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashSet;

/**
 * @author godotg
 * @version 3.0
 */
@Ignore
public class ExportExcelGodot {

    public static final String scanPackage = "com.zfoo.tank.single.boot.godot";
    public static final String excelPath = "classpath:/excel";
    public static final String generateStoragePath = "D:\\github\\godot-bird\\storage";
    public static final String generateBinaryPath = "D:/github/godot-bird/godot_resource_binary.cfg";

    @Test
    public void export() {
        var config = new StorageConfig();
        config.setScanPackage(scanPackage);
        config.setResourceLocation(excelPath);
        config.setWriteable(true);
        config.setRecycle(false);
        var storageManager = new StorageManager();
        storageManager.setStorageConfig(config);
        storageManager.initBefore();
        storageManager.initAfter();

        // 生成协议
        var protocols = new HashSet<Class<?>>();
        protocols.add(ResourceStorage.class);
        protocols.addAll(storageManager.storageMap().keySet());
        var operation = new GenerateOperation();
        operation.setProtocolPath(generateStoragePath);
        operation.getGenerateLanguages().add(CodeLanguage.GdScript);
        ProtocolManager.initProtocolAuto(protocols, operation);

        // 生成数据
        var resourceData = ExportUtils.autoWrapData(ResourceStorage.class, storageManager.storageMap());
        var buffer = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, 100, Integer.MAX_VALUE);
        ProtocolManager.write(buffer, resourceData);
        var bytes = ByteBufUtils.readAllBytes(buffer);
        FileUtils.writeInputStreamToFile(new File(generateBinaryPath), new ByteArrayInputStream(bytes));

        var storage = (Storage<Integer, GodotResource>) storageManager.getStorage(GodotResource.class);
        for (var resource : storage.getAll()) {
            System.out.println(JsonUtils.object2String(resource));
        }
    }

}
