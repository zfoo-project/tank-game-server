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

import com.mongodb.client.model.Filters;
import com.zfoo.orm.OrmContext;
import com.zfoo.orm.query.Page;
import com.zfoo.protocol.model.Pair;
import com.zfoo.protocol.util.AssertionUtils;
import com.zfoo.tank.admin.model.PlayerVO;
import com.zfoo.tank.admin.service.LoginService;
import com.zfoo.tank.common.entity.PlayerEntity;
import com.zfoo.tank.common.result.BaseResponse;
import com.zfoo.tank.common.result.CodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2021-04-12 18:14
 */
@Controller
@CrossOrigin
public class PlayerController {

    @Autowired
    private LoginService loginService;

    @GetMapping(value = "/api/player/search")
    @ResponseBody
    public BaseResponse search(HttpServletRequest request, @RequestParam("query") String query, @RequestParam("page") int page, @RequestParam("itemsPerPage") int itemsPerPage) {
        var adminEntity = loginService.adminUserInfo(request);
        AssertionUtils.notNull(adminEntity);

        //游戏名查询
        var filters = Filters.regex("name", Pattern.compile(query));

        var collection = OrmContext.getOrmManager().getCollection(PlayerEntity.class);

        var count = collection.countDocuments(filters);

        var p = Page.valueOf(page, itemsPerPage, count);

        var result = new ArrayList<PlayerVO>();

        collection.find(filters)
                .skip(p.skipNum())
                .limit(itemsPerPage)
                .forEach(new Consumer<PlayerEntity>() {
                    @Override
                    public void accept(PlayerEntity playerEntity) {
                        result.add(PlayerVO.valueOf(playerEntity));
                    }
                });

        return BaseResponse.valueOf(CodeEnum.OK, new Pair<>(p, result));
    }
}
