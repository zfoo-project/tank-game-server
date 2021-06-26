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

package com.zfoo.tank.admin.service;


import com.zfoo.orm.OrmContext;
import com.zfoo.protocol.collection.ArrayUtils;
import com.zfoo.protocol.util.AssertionUtils;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.tank.admin.model.SignInResponse;
import com.zfoo.tank.admin.model.entity.AdminEntity;
import com.zfoo.tank.common.result.BaseResponse;
import com.zfoo.tank.common.result.CodeEnum;
import com.zfoo.util.security.AesUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2019-12-03 10:54
 */
@Component
public class LoginService {


    public static final String ZGAME_ADMIN_TOKEN = "tank";


    public BaseResponse signIn(String userName, String password) {
        if (StringUtils.isBlank(userName)) {
            return BaseResponse.valueOf(CodeEnum.SIGN_IN_FAIL);
        }

        var adminEntity = OrmContext.getAccessor().load(userName, AdminEntity.class);
        if (adminEntity == null) {
            adminEntity = AdminEntity.valueOf(userName, password);
            OrmContext.getAccessor().insert(adminEntity);
        }

//        if (!password.equals(adminEntity.getPassword())) {
//            return BaseResponse.valueOf(CodeEnum.SIGN_IN_FAIL);
//        }

        var token = AesUtils.getEncryptString(userName + StringUtils.VERTICAL_BAR + password);
        return BaseResponse.valueOf(CodeEnum.OK, SignInResponse.valueOf(token));
    }


    public AdminEntity adminUserInfo(String token) {
        var originStr = AesUtils.getDecryptString(token);
        var userName = StringUtils.substringBeforeFirst(originStr, StringUtils.VERTICAL_BAR);
        var password = StringUtils.substringAfterFirst(originStr, StringUtils.VERTICAL_BAR);

        var adminEntity = OrmContext.getAccessor().load(userName, AdminEntity.class);

        AssertionUtils.isTrue(adminEntity.getPassword().equals(password));

        return adminEntity;
    }

    public AdminEntity adminUserInfo(HttpServletRequest request) {
        // 读取cookie
        var cookies = request.getCookies();
        if (ArrayUtils.isEmpty(cookies)) {
            return null;
        }
        var adminCookie = Arrays.stream(cookies).filter(it -> it.getName().equals(ZGAME_ADMIN_TOKEN)).findFirst();

        if (adminCookie.isEmpty()) {
            return null;
        }

        var adminToken = adminCookie.get().getValue();

        if (StringUtils.isBlank(adminToken)) {
            return null;
        }
        return adminUserInfo(adminToken);
    }

}
