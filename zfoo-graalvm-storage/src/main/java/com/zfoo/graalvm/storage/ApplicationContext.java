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

import com.zfoo.graalvm.storage.resource.StudentCsvResource;
import com.zfoo.graalvm.storage.resource.TestResource;
import com.zfoo.protocol.util.AssertionUtils;
import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.protocol.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

/**
 * java -Dspring.aot.enabled=true -agentlib:native-image-agent=config-output-dir=D:/ -jar zfoo-graalvm-storage-1.0.jar
 */
@SpringBootApplication
public class ApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    public static void main(String[] args) {
        var context = SpringApplication.run(ApplicationContext.class, args);

        var studentManager = context.getBean(StudentManager.class);
        var studentCsvResources = studentManager.studentCsvResources;
        // @Resource注解没指定别名，类名称和Excel名称必须完全一致，没有使用@ExcelFieldName对象属性名会自动对应同名的资源文件列名

        // 通过id找到对应的行
        logger.info(StringUtils.MULTIPLE_HYPHENS);

        // 通过索引找对应的行
        var csvValuesByIndex = studentCsvResources.getIndexes(StudentCsvResource::getName, "james0");
        logger.info(JsonUtils.object2String(csvValuesByIndex));

        // Excel的映射内容需要在被Spring管理的bean的方法上加上@ResInjection注解，即可自动注入Excel对应的对象
        var testManager = context.getBean(TestManager.class);
        var testResources = testManager.testResources;
        for (TestResource resource : testResources.getAll()) {
            Map<Integer, String> map = resource.getType9();
            AssertionUtils.notNull(map.get(1));
            logger.info(JsonUtils.object2String(resource));
        }
        // 通过id找到对应的行
        logger.info(JsonUtils.object2String(testResources.get(2)));
        logger.info(StringUtils.MULTIPLE_HYPHENS);

        var studentResources = studentManager.studentResources;
        for (var resource : studentResources.getAll()) {
            logger.info(JsonUtils.object2String(resource));
        }
    }

}
