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

package com.zfoo.tank.admin.model.setting;

import com.zfoo.protocol.util.AssertionUtils;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2020-03-03 15:48
 */
public enum LanguageEnum {

    /**
     * 中文
     */
    CHINESE((byte) 0),

    /**
     * 英文
     */
    ENGLISH((byte) 1),

    ;

    private static Map<Byte, LanguageEnum> typeMap = new HashMap<>();

    static {
        for (var languageEnum : LanguageEnum.values()) {
            var previousValue = typeMap.putIfAbsent(languageEnum.type, languageEnum);
            AssertionUtils.isNull(previousValue, "LanguageEnum中不应该含有重复type的枚举类[{}]和[{}]", languageEnum, previousValue);
        }
    }

    private byte type;

    LanguageEnum(byte type) {
        this.type = type;
    }

    @Nullable
    public static LanguageEnum getLanguageEnumByType(byte type) {
        return typeMap.get(type);
    }

}
