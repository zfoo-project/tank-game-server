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

import com.zfoo.net.NetContext;
import com.zfoo.protocol.collection.ArrayUtils;
import com.zfoo.protocol.util.AssertionUtils;
import com.zfoo.protocol.util.FileUtils;
import com.zfoo.protocol.util.IOUtils;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.storage.model.resource.ResourceEnum;
import com.zfoo.storage.model.vo.StorageObject;
import com.zfoo.tank.admin.service.LoginService;
import com.zfoo.tank.common.constant.GameConstant;
import com.zfoo.tank.common.constant.TankDeployEnum;
import com.zfoo.tank.common.result.BaseResponse;
import com.zfoo.tank.common.result.CodeEnum;
import com.zfoo.tank.common.util.HotUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2021-04-13 11:02
 */
@Controller
@CrossOrigin
public class ExcelHotswapController {


    @Autowired
    private LoginService loginService;

    @Value("${spring.profiles.active}")
    private TankDeployEnum deployEnum;

    /**
     * Excel热更新
     */
    @PostMapping("/api/excel/hotswap")
    @ResponseBody
    public Object excelHotswap(HttpServletRequest request, @RequestParam MultipartFile[] files) throws IOException {
        var adminEntity = loginService.adminUserInfo(request);
        AssertionUtils.notNull(adminEntity);

        if (ArrayUtils.isEmpty(files)) {
            return BaseResponse.valueOf(CodeEnum.FAIL, "上传的excel文件是空的");
        }


        if (deployEnum == TankDeployEnum.zfoo) {
            return BaseResponse.valueOf(CodeEnum.PARAMETER_ENV_ERROR);
        }

        var clazzSimpleNameMap = HotUtils.configSimpleClazzNameMap();

        var newFiles = new ArrayList<MultipartFile>();
        // 先检查excel文件的格式
        for (var file : files) {
            var fileSimpleName = FileUtils.fileSimpleName(file.getOriginalFilename());
            var fileExtName = FileUtils.fileExtName(file.getOriginalFilename());
            if (!ResourceEnum.containsResourceEnum(fileExtName)) {
                return BaseResponse.valueOf(CodeEnum.FAIL, StringUtils.format("文件[{}]必须是[{}]格式的", file.getOriginalFilename(), ResourceEnum.typesToString()));
            }

            if (clazzSimpleNameMap.containsKey(fileSimpleName)) {
                newFiles.add(file);
            }
        }

        // 先解析一下配置表，看看配置表是否合法
        for (var file : newFiles) {
            try {
                var fileSimpleName = FileUtils.fileSimpleName(file.getOriginalFilename());
                var clazz = clazzSimpleNameMap.get(fileSimpleName);
                var inputStream = file.getInputStream();
                var fileExtName = FileUtils.fileExtName(file.getOriginalFilename());
                StorageObject<?, ?> storage = StorageObject.parse(inputStream, clazz, fileExtName);
            } catch (Exception e) {
                return BaseResponse.valueOf(CodeEnum.FAIL, StringUtils.format("excel文件[{}]解析出错[{}]", file.getOriginalFilename(), e.getMessage()));
            }
        }


        // 将配置表上传的zookeeper中，其它节点会监听配置表的变化
        for (var file : newFiles) {
            var fileName = file.getOriginalFilename();
            var inputStream = file.getInputStream();

            var path = StringUtils.format("{}/{}", GameConstant.EXCEL_HOTSWAP_ZK_PATH, fileName);
            var bytes = IOUtils.toByteArray(inputStream);
            NetContext.getConfigManager().getRegistry().addData(path, bytes, CreateMode.EPHEMERAL);
        }

        return BaseResponse.valueOf(CodeEnum.OK, null);
    }

}
