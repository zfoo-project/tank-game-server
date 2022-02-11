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

package com.zfoo.tank.cache.controller;

import com.zfoo.event.manager.EventBus;
import com.zfoo.net.NetContext;
import com.zfoo.net.core.gateway.model.AuthUidToGatewayCheck;
import com.zfoo.net.core.gateway.model.AuthUidToGatewayConfirm;
import com.zfoo.net.packet.common.Error;
import com.zfoo.net.router.attachment.GatewayAttachment;
import com.zfoo.net.router.receiver.PacketReceiver;
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
import com.zfoo.util.math.HashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @PacketReceiver
    public void atLogoutRequest(Session session, LogoutRequest request, GatewayAttachment gatewayAttachment) {
        logger.info("c[{}][{}]玩家退出游戏", gatewayAttachment.getUid(), gatewayAttachment.getSid());

        var uid = gatewayAttachment.getUid();
        var player = playerEntityCaches.load(uid);
        player.sid = 0;
        player.session = null;
        playerEntityCaches.update(player);
    }

    @PacketReceiver
    public void atLoginRequest(Session session, LoginRequest request, GatewayAttachment gatewayAttachment) {
        var account = StringUtils.trim(request.getAccount());
        var password = request.getPassword();

        if (StringUtils.isBlank(account)) {
            NetContext.getRouter().send(session, Error.valueOf(I18nEnum.error_account_not_exist.toString()), gatewayAttachment);
            return;
        }

        logger.info("c[{}][{}]玩家登录[account:{}][password:{}]", gatewayAttachment.getUid(), gatewayAttachment.getSid(), account, password);

        EventBus.execute(HashUtils.fnvHash(account)).execute(new Runnable() {
            @Override
            public void run() {
                var accountEntity = OrmContext.getAccessor().load(account, AccountEntity.class);
                if (accountEntity == null) {
                    var id = MongoIdUtils.getIncrementIdFromMongoDefault(PlayerEntity.class);

                    OrmContext.getAccessor().insert(PlayerEntity.valueOf(id, account, 1, TimeUtils.now(), TimeUtils.now()));
                    accountEntity = AccountEntity.valueOf(account, account, password, id);
                    OrmContext.getAccessor().insert(accountEntity);
                }

                if (deployEnum == TankDeployEnum.pro) {
                    // 验证密码
                    if (StringUtils.isNotBlank(accountEntity.getPassword()) && !accountEntity.getPassword().trim().equals(password.trim())) {
                        logger.info("[id:{}][password:{}]账号或密码错误", accountEntity.getUid(), password);
                        NetContext.getRouter().send(session, Error.valueOf(I18nEnum.error_account_password.toString()), gatewayAttachment);
                        return;
                    }
                }
                // 授权给网关uid
                NetContext.getRouter().send(session, AuthUidToGatewayCheck.valueOf(accountEntity.getUid()), gatewayAttachment);
            }
        });
    }

    @PacketReceiver
    public void atAuthUidToGatewayConfirm(Session session, AuthUidToGatewayConfirm confirm, GatewayAttachment gatewayAttachment) {
        var uid = confirm.getUid();
        var sid = gatewayAttachment.getSid();
        if (uid <= 0) {
            logger.error("授权[uid:{}]异常", uid);
            return;
        }

        var player = playerEntityCaches.load(uid);
        player.setLastLoginTime(TimeUtils.now());
        player.sid = sid;
        player.session = session;
        playerEntityCaches.update(player);
        if (player.id() <= 0) {
            NetContext.getRouter().send(session, Error.valueOf(I18nEnum.error_account_not_exist.toString()), gatewayAttachment);
            return;
        }

        var token = TokenUtils.set(uid);
        NetContext.getRouter().send(session, LoginResponse.valueOf(token), gatewayAttachment);
        NetContext.getRouter().send(session, GetPlayerInfoResponse.valueOf(player.toPlayerInfo(), player.getCurrencyPo().toCurrencyVO()), gatewayAttachment);
    }

    @PacketReceiver
    public void atGetPlayerInfoRequest(Session session, GetPlayerInfoRequest request, GatewayAttachment gatewayAttachment) {
        var token = request.getToken();

        logger.info("c[{}][{}]玩家信息[token:{}]", gatewayAttachment.getUid(), gatewayAttachment.getSid(), token);

        if (StringUtils.isBlank(token)) {
            NetContext.getRouter().send(session, Error.valueOf(I18nEnum.error_protocol_param.toString()), gatewayAttachment);
            return;
        }

        var triple = TokenUtils.get(token);
        var playerId = triple.getLeft();

        // 授权给网关uid
        NetContext.getRouter().send(session, AuthUidToGatewayCheck.valueOf(playerId), gatewayAttachment);
    }

}
