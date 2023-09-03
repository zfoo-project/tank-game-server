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

import com.zfoo.graalvm.protocol.packet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.BindingReflectionHintsRegistrar;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import java.util.HashSet;

/**
 * Register runtime hints for the token library
 *
 * @author godotg
 */
public class ProtocolHints implements RuntimeHintsRegistrar {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolHints.class);

    private final BindingReflectionHintsRegistrar bindingRegistrar = new BindingReflectionHintsRegistrar();

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        logger.info("protocol graalvm aot runtime hints register");

        var classes = new HashSet<Class<?>>();
        classes.add(ComplexObject.class);
        classes.add(EmptyObject.class);
        classes.add(NormalObject.class);
        classes.add(ObjectA.class);
        classes.add(ObjectB.class);
        classes.add(SimpleObject.class);

        for (var clazz : classes) {
            this.bindingRegistrar.registerReflectionHints(hints.reflection(), clazz);
            logger.info("protocol graalvm aot hints register serialization [{}]", clazz);
        }

        var include = "*.bytes";
        hints.resources().registerPattern(include);
        logger.info("protocol graalvm aot hints register resources [{}]", include);
    }
}
