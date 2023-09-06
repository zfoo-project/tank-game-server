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

package com.zfoo.graalvm.protocol;

import com.zfoo.graalvm.protocol.packet.ComplexObject;
import com.zfoo.graalvm.protocol.packet.NormalObject;
import com.zfoo.graalvm.protocol.packet.SimpleObject;
import com.zfoo.protocol.ProtocolManager;
import com.zfoo.protocol.generate.GenerateOperation;
import com.zfoo.protocol.util.ClassUtils;
import com.zfoo.protocol.util.IOUtils;
import com.zfoo.protocol.util.StringUtils;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

import java.io.IOException;
import java.util.Set;

/**
 * 需要先使用下面这行生成hint文件
 * <p>
 * java -Dspring.aot.enabled=true -agentlib:native-image-agent=config-output-dir=D:/ -jar zfoo-graalvm-protocol-1.0.jar
 */
@SpringBootApplication
public class ApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ApplicationContext.class, args);

        var bytes = IOUtils.toByteArray(ClassUtils.getFileFromClassPath("ComplexObject.bytes"));
        logger.info("字节数组读取长度 [{}]", bytes.length);

        var buffer = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, 100, 1_0000);
        buffer.writeBytes(bytes);

        ProtocolManager.initProtocolAuto(Set.of(ComplexObject.class, NormalObject.class, SimpleObject.class), GenerateOperation.NO_OPERATION);

        var packet = ProtocolManager.read(buffer);

        var newBuffer = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, 100, 1_0000);
        ProtocolManager.write(newBuffer, packet);

        buffer.resetReaderIndex();
        newBuffer.resetReaderIndex();

        // 字节码增强的Map遍历顺序会出现不一样，所以序列化的内容顺序会改变，可以看到不相同的字节并不是连续的
        var equal = 0;
        var notEqual = 0;
        for (int i = 0; i < buffer.writerIndex(); i++) {
            var a = buffer.readByte();
            var b = newBuffer.readByte();
            if (a == b) {
                equal++;
            } else {
                notEqual++;
            }
        }
        logger.info(StringUtils.format("equal [{}], not equal [{}]", equal, notEqual));
    }

}
