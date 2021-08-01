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

package com.zfoo.tank.admin.controller;

import com.zfoo.protocol.util.StringUtils;
import com.zfoo.tank.admin.model.SignInRequest;
import com.zfoo.tank.admin.service.LoginService;
import com.zfoo.tank.common.result.BaseResponse;
import com.zfoo.tank.common.result.CodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2021-02-04 17:27
 */
@Controller
@CrossOrigin
public class UserController {

    @Autowired
    private LoginService loginService;


    @PostMapping(value = "/api/signIn", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public BaseResponse signIn(@RequestBody SignInRequest signIn) {

        if (StringUtils.isBlank(signIn.getUserName()) || StringUtils.isBlank(signIn.getPassword())) {
            return BaseResponse.valueOf(CodeEnum.SIGN_IN_FAIL);
        }

        return loginService.signIn(signIn.getUserName(), signIn.getPassword());
    }

    @GetMapping(value = "/api/user")
    @ResponseBody
    public BaseResponse userInfo(@RequestParam("token") String token) {
        if (StringUtils.isBlank(token)) {
            return BaseResponse.valueOf(CodeEnum.FAIL, null);
        }

        var adminEntity = loginService.adminUserInfo(token);

        return BaseResponse.valueOf(CodeEnum.OK, adminEntity);
    }

    @PostMapping(value = "/api/logout")
    @ResponseBody
    public BaseResponse userInfo(HttpServletRequest request) {
        var adminEntity = loginService.adminUserInfo(request);
        return BaseResponse.valueOf(CodeEnum.OK);
    }

}
