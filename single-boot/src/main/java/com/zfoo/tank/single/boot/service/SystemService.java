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

package com.zfoo.tank.single.boot.service;

import com.zfoo.event.model.AppStartEvent;
import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.protocol.collection.tree.DfaWordTree;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.storage.anno.StorageAutowired;
import com.zfoo.storage.manager.IStorage;
import com.zfoo.tank.common.resource.FilterWordResource;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author yidingzhao
 * @version 1.0
 * @since 2021-02-05 14:10
 */
@Component
public class SystemService implements ApplicationListener<AppStartEvent> {

    public DfaWordTree dfaWordTree;

    // 中文正则
    public Pattern cnPattern = Pattern.compile("[\u4E00-\u9FA5]");

    // 下面是关键词过滤相关
    @StorageAutowired
    private IStorage<Integer, FilterWordResource> filterWordResources;

    @Override
    public void onApplicationEvent(AppStartEvent event) {
        var tree = new DfaWordTree();
        var words = filterWordResources.getAll().stream()
                .map(it -> it.getFilter().trim().toLowerCase())
                .collect(Collectors.toList());
        tree.addWords(words);
        dfaWordTree = tree;
    }

    /**
     * 铭感词过滤
     *
     * @param word 需要过滤的铭感词
     * @return true有铭感词，false没有铭感词
     */
    public boolean hasSensitiveWord(String word) {
        if (StringUtils.isBlank(word)) {
            return false;
        }

        // 一律转换成小写
        word = word.trim().toLowerCase();

        // 先过滤中文，再过滤英文
        var cnStr = new StringBuilder();
        var enStr = new StringBuilder();
        word.chars().forEach(it -> {
            var c = Character.valueOf((char) it);
            var s = String.valueOf(Character.valueOf((char) it));
            if (cnPattern.matcher(s).matches()) {
                cnStr.append(s);
            } else if (StringUtils.isStopChar(c)) {
                cnStr.append(s);
            } else {
                enStr.append(s);
            }
        });

        var result = new ArrayList<String>();
        result.addAll(dfaWordTree.matchAll(word, -1, true, true));

        if (CollectionUtils.isNotEmpty(result)) {
            return true;
        }

        if (StringUtils.isNotBlank(cnStr)) {
            result.addAll(dfaWordTree.matchAll(cnStr.toString(), -1, true, true));
        }

        if (StringUtils.isNotBlank(enStr)) {
            result.addAll(dfaWordTree.matchAll(enStr.toString(), -1, true, true));
        }

        return CollectionUtils.isNotEmpty(result);
    }

}
