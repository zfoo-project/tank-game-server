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

package com.zfoo.tank.home.controller;

import com.zfoo.event.manager.EventBus;
import com.zfoo.net.NetContext;
import com.zfoo.net.packet.common.Error;
import com.zfoo.net.router.attachment.GatewayAttachment;
import com.zfoo.net.anno.PacketReceiver;
import com.zfoo.net.session.Session;
import com.zfoo.net.util.HashUtils;
import com.zfoo.net.util.NetUtils;
import com.zfoo.orm.OrmContext;
import com.zfoo.orm.anno.EntityCacheAutowired;
import com.zfoo.orm.cache.IEntityCache;
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
import com.zfoo.tank.common.result.CodeEnum;
import com.zfoo.tank.common.util.HttpLoginUtils;
import com.zfoo.tank.common.util.TokenUtils;
import com.zfoo.tank.home.service.PlayerService;
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
    private IEntityCache<Long, PlayerEntity> playerEntityCaches;

    @Value("${spring.profiles.active}")
    private TankDeployEnum deployEnum;
    @Autowired
    private PlayerService playerService;

    @PacketReceiver
    public void atLoginByHttpTokenRequest(Session session, LoginByHttpTokenRequest request, GatewayAttachment attachment) {
        var token = request.getToken();
        var reason = request.getReason();
        var confVersion = request.getConfVersion();
        var resourceVersion = request.getResourceVersion();
        var appVersion = StringUtils.trim(request.getAppVersion());
        var ip = request.getIp();


        if (StringUtils.isBlank(token)) {
            NetContext.getRouter().send(session, LoginByHttpTokenResponse.valueOf(CodeEnum.error_2.getMessage()), attachment);
            return;
        }
        var loginInfo = HttpLoginUtils.fromToken(token);
        var uid = loginInfo.getUid();
        var name = loginInfo.getName();
        var expireTime = loginInfo.getExpireTime();

        var sid = attachment.getSid();

        logger.info("c[{}][{}] atLoginByHttpTokenRequest [token:{}][ip:{}][appVersion:{}] [uid:{}] [expireTime:{}]", uid, sid, token, ip, appVersion, uid, expireTime);


        if (TimeUtils.now() >= expireTime) {
            NetContext.getRouter().send(session, LoginByHttpTokenResponse.valueOf(CodeEnum.error_2.getMessage()), attachment);
            return;
        }


        var playerEntity = playerEntityCaches.load(uid);

        // 从sdk登录，走特殊流程
        if (playerEntity.getId() <= 0) {
            var newUserEntity = PlayerEntity.valueOf(uid, name, 1, TimeUtils.now(),TimeUtils.now());
            OrmContext.getAccessor().insert(newUserEntity);
            playerEntityCaches.invalidate(uid);
            playerEntity = playerEntityCaches.load(uid);
        }

        loginBefore(playerEntity, session, attachment);
        NetContext.getRouter().send(session, LoginByHttpTokenResponse.valueOf(CodeEnum.OK.getMessage()), attachment);
    }

    public void loginBefore(PlayerEntity playerEntity, Session session, GatewayAttachment attachment) {
        // 释放之前的tcp连接，单点登录
        var gateway = playerEntity.getGsid();
        var oldSession = NetContext.getSessionManager().getServerSession(gateway.getConsumerSid());
        if (oldSession != null) {
            NetContext.getRouter().send(oldSession, new KickPlayerAsk(gateway.getGatewaySid(), playerEntity.getId()), null);
        }
        // 更新现在的路由信息
        gateway.update(session.getSid(), attachment.getSid());
    }

    @PacketReceiver
    public void atLogoutRequest(Session session, LogoutRequest request, GatewayAttachment gatewayAttachment) {
        logger.info("c[{}][{}]玩家退出游戏", gatewayAttachment.getUid(), gatewayAttachment.getSid());

        var uid = gatewayAttachment.getUid();
        var player = playerEntityCaches.load(uid);
        playerEntityCaches.update(player);
    }


    @PacketReceiver
    public void atGetPlayerInfoRequest(Session session, GetPlayerInfoRequest request, GatewayAttachment gatewayAttachment) {
        var sid = gatewayAttachment.getSid();
        var uid = gatewayAttachment.getSid();

        logger.info("c[{}][{}]玩家信息获取", uid, sid);

        var player = playerService.loadPlayer(session, gatewayAttachment);
        player.setLastLoginTime(TimeUtils.now());
        playerEntityCaches.update(player);
        if (player.id() <= 0) {
            NetContext.getRouter().send(session, Error.valueOf(I18nEnum.error_account_not_exist.toString()), gatewayAttachment);
            return;
        }

        // 授权给网关uid
        NetContext.getRouter().send(session, GetPlayerInfoResponse.valueOf(player.toPlayerInfo(), player.getCurrencyPo().toCurrencyVO()), gatewayAttachment);
    }

}
