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
public enum ThemeEnum {

    /**
     * 黑色
     */
    DARK((byte) 0),

    /**
     * 白色
     */
    LIGHT((byte) 1),

    ;

    private static Map<Byte, ThemeEnum> typeMap = new HashMap<>();

    static {
        for (var themeEnum : ThemeEnum.values()) {
            var previousValue = typeMap.putIfAbsent(themeEnum.type, themeEnum);
            AssertionUtils.isNull(previousValue, "ThemeEnum中不应该含有重复type的枚举类[{}]和[{}]", themeEnum, previousValue);
        }
    }

    private byte type;

    ThemeEnum(byte type) {
        this.type = type;
    }

    @Nullable
    public static ThemeEnum getThemeEnumByType(byte type) {
        return typeMap.get(type);
    }

    public byte getType() {
        return type;
    }
}
