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

import com.zfoo.orm.OrmContext;
import com.zfoo.protocol.util.AssertionUtils;
import com.zfoo.tank.admin.model.setting.LanguageEnum;
import com.zfoo.tank.admin.model.setting.SaveSettingRequest;
import com.zfoo.tank.admin.model.setting.ThemeEnum;
import com.zfoo.tank.admin.service.LoginService;
import com.zfoo.tank.common.result.BaseResponse;
import com.zfoo.tank.common.result.CodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2021-03-04 19:50
 */
@Controller
public class SettingController {

    @Autowired
    private LoginService loginService;

    /**
     * 保存自定义设置
     *
     * @param cm 保存自定义设置请求
     * @return 一个空的对象
     */
    @PostMapping(value = "/api/user/saveSetting", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public BaseResponse saveSetting(HttpServletRequest request, @RequestBody SaveSettingRequest cm) throws Exception {
        var adminEntity = loginService.adminUserInfo(request);
        AssertionUtils.notNull(adminEntity);

        var theme = cm.getTheme();
        var language = cm.getLanguage();

        if (ThemeEnum.getThemeEnumByType(theme) == null) {
            return BaseResponse.valueOf(CodeEnum.PARAMETER_ERROR);
        }

        if (LanguageEnum.getLanguageEnumByType(language) == null) {
            return BaseResponse.valueOf(CodeEnum.PARAMETER_ERROR);
        }

        var settingPO = adminEntity.getSetting();
        settingPO.setTheme(theme);
        settingPO.setLanguage(language);

        OrmContext.getAccessor().update(adminEntity);
        return BaseResponse.valueOf(CodeEnum.OK);
    }

}
