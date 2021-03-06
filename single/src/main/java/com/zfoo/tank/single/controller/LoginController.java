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
import com.zfoo.net.session.model.AttributeType;
import com.zfoo.net.session.model.Session;
import com.zfoo.orm.OrmContext;
import com.zfoo.orm.model.anno.EntityCachesInjection;
import com.zfoo.orm.model.cache.IEntityCaches;
import com.zfoo.orm.util.MongoIdUtils;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.scheduler.util.TimeUtils;
import com.zfoo.storage.model.anno.ResInjection;
import com.zfoo.storage.model.vo.Storage;
import com.zfoo.tank.common.constant.I18nEnum;
import com.zfoo.tank.common.constant.TankDeployEnum;
import com.zfoo.tank.common.entity.AccountEntity;
import com.zfoo.tank.common.entity.PlayerEntity;
import com.zfoo.tank.common.protocol.login.*;
import com.zfoo.tank.common.resource.PropertyResource;
import com.zfoo.tank.common.util.TokenUtils;
import com.zfoo.tank.single.service.SystemService;
import com.zfoo.util.math.HashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author jaysunxiao
 * @version 1.0
 * @since 2021-01-20 14:43
 */
@Component
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @ResInjection
    public Storage<String, PropertyResource> propertyResources;

    @EntityCachesInjection
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

        // ??????????????????????????????????????????????????????????????????????????????????????????
        var sid = session.getSid();

        // ??????uid????????????????????????????????????account???????????????
        EventBus.execute(HashUtils.fnvHash(account)).execute(() -> {
            var accountEntity = OrmContext.getAccessor().load(account, AccountEntity.class);
            if (accountEntity == null) {
                // uid
                var newUid = MongoIdUtils.getIncrementIdFromMongoDefault(PlayerEntity.class);

                // ??????AccountEntity(????????????????????????????????????????????????????????????????????????????????????)
                accountEntity = AccountEntity.valueOf(account, account, password, newUid);
                OrmContext.getAccessor().insert(accountEntity);

                // ?????????PlayerEntity(?????????????????????????????????,???????????????????????????????????????????????????)
                OrmContext.getAccessor().insert(PlayerEntity.valueOf(newUid, account, 1, TimeUtils.now(), TimeUtils.now()));
            }

            if (deployEnum == TankDeployEnum.pro) {
                // ????????????
                if (StringUtils.isNotBlank(accountEntity.getPassword()) && !accountEntity.getPassword().trim().equals(password.trim())) {
                    logger.info("[id:{}][password:{}]?????????????????????", accountEntity.getUid(), password);
                    NetContext.getRouter().send(session, Error.valueOf(I18nEnum.error_account_password.toString()));
                    return;
                }
            }

            var uid = accountEntity.getUid();
            logger.info("c[{}][{}]????????????[account:{}][password:{}]", uid, sid, account, password);

            session.putAttribute(AttributeType.UID, accountEntity.getUid());

            // ?????????????????????????????????????????????????????????
            var player = playerEntityCaches.load(uid);
            player.setLastLoginTime(TimeUtils.now());

            // ??????sid???session(???2??????????????????)
            player.sid = sid;
            player.session = session;

            session.putAttribute(AttributeType.UID, uid);

            playerEntityCaches.update(player);

            if (player.id() <= 0) {
                NetContext.getRouter().send(session, Error.valueOf(I18nEnum.error_account_not_exist.toString()));
                return;
            }

            var token = TokenUtils.set(uid);
            NetContext.getRouter().send(session, LoginResponse.valueOf(token));
            NetContext.getRouter().send(session, GetPlayerInfoResponse.valueOf(player.toPlayerInfo(), player.getCurrencyPo().toCurrencyVO()));
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

        logger.info("c[{}][{}]????????????[token:{}]", uid, sid, token);

        var player = playerEntityCaches.load(uid);

        // ??????session
        player.sid = sid;
        player.session = session;
        session.putAttribute(AttributeType.UID, uid);

        NetContext.getRouter().send(session, LoginResponse.valueOf(token));
        NetContext.getRouter().send(session, GetPlayerInfoResponse.valueOf(player.toPlayerInfo(), player.getCurrencyPo().toCurrencyVO()));
    }

    @PacketReceiver
    public void atLogoutRequest(Session session, LogoutRequest request) {
        var uid = (long) session.getAttribute(AttributeType.UID);
        var sid = session.getSid();

        logger.info("c[{}][{}]??????????????????", uid, sid);

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
