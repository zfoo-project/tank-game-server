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

package com.zfoo.tank.common.resource;

import com.zfoo.storage.anno.GraalvmNativeStorage;
import com.zfoo.storage.anno.Id;
import com.zfoo.storage.anno.Storage;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2020-04-22 14:39
 */
@Storage
@GraalvmNativeStorage("classpath:export/FilterWordResource.csv")
public class FilterWordResource {

    @Id
    private int id;

    private String filter;

    public int getId() {
        return id;
    }

    public String getFilter() {
        return filter;
    }

}
