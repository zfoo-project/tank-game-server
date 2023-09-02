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

package com.zfoo.tank.admin;


import com.zfoo.protocol.util.StringUtils;
import com.zfoo.tank.common.constant.TankDeployEnum;
import com.zfoo.protocol.util.ThreadUtils;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2019-11-07 17:45
 */
@Ignore
public class ApplicationTest {

    static {
        TankDeployEnum.initDefaultEnv();
    }

    @Test
    public void startApplication() {
        Application.main(StringUtils.EMPTY_ARRAY);

        ThreadUtils.sleep(Long.MAX_VALUE);
    }

}
