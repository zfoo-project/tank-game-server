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
import com.zfoo.protocol.generate.GenerateOperation;
import com.zfoo.protocol.serializer.CodeLanguage;
import com.zfoo.protocol.util.ClassUtils;
import com.zfoo.protocol.util.DomUtils;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.protocol.xml.XmlProtocols;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

/**
 * @author godotg
 */
@Ignore
public class ExportProtocolGodot {

    public static final String generateProtocolPath = "D:\\github\\godot-bird\\protocol";
    public static final String protocolLocation = "protocol.xml";

    @Test
    public void exportProtocol() throws IOException {
        // 生成协议
        var xmlProtocols = DomUtils.inputStream2Object(ClassUtils.getFileFromClassPath(protocolLocation), XmlProtocols.class);
        var operation = new GenerateOperation();
        operation.setProtocolPath(generateProtocolPath);
        operation.setFoldProtocol(true);
        operation.getGenerateLanguages().add(CodeLanguage.GdScript);
        ProtocolManager.initProtocol(xmlProtocols, operation);
        var count = 0;
        for (int i = 0; i < ProtocolManager.MAX_PROTOCOL_NUM; i++) {
            if (ProtocolManager.protocols[i] != null) {
                count++;
            }
        }
        System.out.println(StringUtils.format("导出协议成功，导出个数[{}]", count));
    }

}
