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
import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.protocol.util.AssertionUtils;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.tank.admin.model.SignInResponse;
import com.zfoo.tank.admin.model.entity.AdminEntity;
import com.zfoo.tank.common.entity.AccountEntity;
import com.zfoo.tank.common.result.BaseResponse;
import com.zfoo.tank.common.result.CodeEnum;
import com.zfoo.util.math.NumberUtils;
import com.zfoo.util.security.AesUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

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
        var adminToken = request.getHeader(ZGAME_ADMIN_TOKEN);

        if (StringUtils.isBlank(adminToken)) {
            return null;
        }
        return adminUserInfo(adminToken);
    }

    public long getUserIdByAccount(String account) {
        if (StringUtils.isBlank(account)) {
            return 0;
        }
        account = account.trim();

        var list = OrmContext.getQuery().queryFieldEqual("_id", account, AccountEntity.class);
        if (CollectionUtils.isEmpty(list)) {
            if (NumberUtils.isNumeric(account)) {
                return Long.parseLong(account);
            }
            return 0;
        }
        var userId = list.get(0).getUid();
        return userId;
    }
}
