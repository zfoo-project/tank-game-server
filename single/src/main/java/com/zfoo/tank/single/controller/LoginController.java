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

package com.zfoo.tank.single.controller;

import com.zfoo.event.manager.EventBus;
import com.zfoo.net.NetContext;
import com.zfoo.net.packet.common.Error;
import com.zfoo.net.packet.common.Ping;
import com.zfoo.net.packet.common.Pong;
import com.zfoo.net.router.receiver.PacketReceiver;
import com.zfoo.net.session.Session;
import com.zfoo.net.util.HashUtils;
import com.zfoo.orm.OrmContext;
import com.zfoo.orm.anno.EntityCacheAutowired;
import com.zfoo.orm.cache.IEntityCaches;
import com.zfoo.orm.util.MongoIdUtils;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.scheduler.util.TimeUtils;
import com.zfoo.storage.anno.StorageAutowired;
import com.zfoo.storage.model.IStorage;
import com.zfoo.tank.common.constant.I18nEnum;
import com.zfoo.tank.common.constant.TankDeployEnum;
import com.zfoo.tank.common.entity.AccountEntity;
import com.zfoo.tank.common.entity.PlayerEntity;
import com.zfoo.tank.common.protocol.login.*;
import com.zfoo.tank.common.resource.PropertyResource;
import com.zfoo.tank.common.util.TokenUtils;
import com.zfoo.tank.single.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author godotg
 * @version 1.0
 * @since 2021-01-20 14:43
 */
@Component
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @StorageAutowired
    public IStorage<String, PropertyResource> propertyResources;

    @EntityCacheAutowired
    private IEntityCaches<Long, PlayerEntity> playerEntityCaches;

    @Value("${spring.profiles.active}")
    private TankDeployEnum deployEnum;

    @Autowired
    private SystemService systemService;


    @PacketReceiver
    public void atLoginRequest(Session session, LoginRequest request) {
        var account = StringUtils.trim(request.getAccount());
        var password = request.getPassword();

        if (StringUtils.isBlank(account)) {
            NetContext.getRouter().send(session, Error.valueOf(I18nEnum.error_account_not_exist.toString()));
            return;
        }

        if (systemService.hasSensitiveWord(account)) {
            NetContext.getRouter().send(session, Error.valueOf(I18nEnum.error_account_sensitive_word.toString()));
            return;
        }

        // 这个是连接上服务器都自动分配的，每次连接上服务器都会分配一个
        var sid = session.getSid();

        // 由于uid还没确定下来，现在只能用account线程去处理
        EventBus.execute(HashUtils.fnvHash(account), new Runnable() {
            @Override
            public void run() {
                var accountEntity = OrmContext.getAccessor().load(account, AccountEntity.class);
                if (accountEntity == null) {
                    // uid
                    var newUid = MongoIdUtils.getIncrementIdFromMongoDefault(PlayerEntity.class);

                    // 插入AccountEntity(比如：微信登录，没这个微信对应的游戏账号，先生成账号信息)
                    accountEntity = AccountEntity.valueOf(account, account, password, newUid);
                    OrmContext.getAccessor().insert(accountEntity);

                    // 先插入PlayerEntity(这个是游戏内的玩家信息,比如：金币、经验、等级、头像等信息)
                    OrmContext.getAccessor().insert(PlayerEntity.valueOf(newUid, account, 1, TimeUtils.now(), TimeUtils.now()));
                }

                if (deployEnum == TankDeployEnum.pro) {
                    // 验证密码
                    if (StringUtils.isNotBlank(accountEntity.getPassword()) && !accountEntity.getPassword().trim().equals(password.trim())) {
                        logger.info("[id:{}][password:{}]账号或密码错误", accountEntity.getUid(), password);
                        NetContext.getRouter().send(session, Error.valueOf(I18nEnum.error_account_password.toString()));
                        return;
                    }
                }

                var uid = accountEntity.getUid();
                logger.info("c[{}][{}]玩家登录[account:{}][password:{}]", uid, sid, account, password);

                session.setUid(accountEntity.getUid());

                // 由于之前插入过了，这里是肯定可以获取到
                var player = playerEntityCaches.load(uid);
                player.setLastLoginTime(TimeUtils.now());

                // 设置sid和session(这2个都是临时的)
                player.sid = sid;
                player.session = session;

                session.setUid(uid);

                if (player.id() <= 0) {
                    NetContext.getRouter().send(session, Error.valueOf(I18nEnum.error_account_not_exist.toString()));
                    return;
                }

                var token = TokenUtils.set(uid);
                NetContext.getRouter().send(session, LoginResponse.valueOf(token));
                NetContext.getRouter().send(session, GetPlayerInfoResponse.valueOf(player.toPlayerInfo(), player.getCurrencyPo().toCurrencyVO()));
            }
        });
    }

    @PacketReceiver
    public void atGetPlayerInfoRequest(Session session, GetPlayerInfoRequest request) {
        var token = request.getToken();

        if (StringUtils.isBlank(token)) {
            NetContext.getRouter().send(session, Error.valueOf(I18nEnum.error_protocol_param.toString()));
            return;
        }

        var triple = TokenUtils.get(token);
        var uid = triple.getLeft();
        var sid = session.getSid();

        logger.info("c[{}][{}]玩家信息[token:{}]", uid, sid, token);

        var player = playerEntityCaches.load(uid);

        // 设置session
        player.sid = sid;
        player.session = session;
        session.setUid(uid);

        NetContext.getRouter().send(session, LoginResponse.valueOf(token));
        NetContext.getRouter().send(session, GetPlayerInfoResponse.valueOf(player.toPlayerInfo(), player.getCurrencyPo().toCurrencyVO()));
    }

    @PacketReceiver
    public void atLogoutRequest(Session session, LogoutRequest request) {
        var uid = session.getUid();
        var sid = session.getSid();

        logger.info("c[{}][{}]玩家退出游戏", uid, sid);

        var player = playerEntityCaches.load(uid);
        player.sid = 0;
        player.session = null;
        playerEntityCaches.update(player);
    }

    @PacketReceiver
    public void atPing(Session session, Ping request) {
        NetContext.getRouter().send(session, Pong.valueOf(TimeUtils.now()));
    }

}
