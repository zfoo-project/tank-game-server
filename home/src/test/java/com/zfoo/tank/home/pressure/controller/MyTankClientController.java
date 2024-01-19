package com.zfoo.tank.home.pressure.controller;

import com.zfoo.net.anno.PacketReceiver;
import com.zfoo.net.session.Session;
import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.tank.common.protocol.CurrencyUpdateNotice;
import com.zfoo.tank.common.protocol.PlayerExpNotice;
import com.zfoo.tank.common.protocol.battle.BattleResultResponse;
import com.zfoo.tank.common.protocol.login.LoginResponse;
import com.zfoo.tank.common.protocol.cache.ScoreRankResponse;
import com.zfoo.tank.common.protocol.login.GetPlayerInfoResponse;
import com.zfoo.tank.home.controller.BattleController;
import com.zfoo.tank.home.pressure.MiniClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author xiaorenping
 */
@Component
public class MyTankClientController {
    private static final Logger logger = LoggerFactory.getLogger(BattleController.class);

    @PacketReceiver
    public void atLoginResponse(Session session, LoginResponse response) {
        MiniClient miniClient = session.getChannel().attr(MiniClient.Attribute_Key).get();
        miniClient.lastTimePing = System.currentTimeMillis();

        miniClient.token = response.getToken();
        logger.info("Ts=================客户端收到服务器的登录令牌返回name={}       [token:{}]", miniClient.account, miniClient.token);
    }


    @PacketReceiver
    public void atGetPlayerInfoResponse(Session session, GetPlayerInfoResponse response) {
        MiniClient miniClient = session.getChannel().attr(MiniClient.Attribute_Key).get();
        miniClient.lastTimePing = System.currentTimeMillis();

        var playerInfo = response.getPlayerInfo();
        var currencyVo = response.getCurrencyVo();
        logger.info("Ts=================玩家基础信息[id:{}][name:{}]", playerInfo.getId(), playerInfo.getName());
        //MyTankData.playerInfo = playerInfo;
        //MyTankData.currencyVo = currencyVo;
    }


    @PacketReceiver
    public void atBattleResultResponse(Session session, BattleResultResponse response) {
        MiniClient miniClient = session.getChannel().attr(MiniClient.Attribute_Key).get();
        miniClient.lastTimePing = System.currentTimeMillis();

        logger.info("Ts=================发送一个战斗结果请求===结果[{}]", JsonUtils.object2String(response));
    }

    @PacketReceiver
    public void atScoreRankResponse(Session session, ScoreRankResponse response) {
        MiniClient miniClient = session.getChannel().attr(MiniClient.Attribute_Key).get();
        miniClient.lastTimePing = System.currentTimeMillis();

        logger.info("Ts=================开始执行连接--->登录请求===排行榜信息[{}]", JsonUtils.object2String(response));
    }

    @PacketReceiver
    public void atCurrencyUpdateNotice(Session session, CurrencyUpdateNotice notice) {
        MiniClient miniClient = session.getChannel().attr(MiniClient.Attribute_Key).get();
        miniClient.lastTimePing = System.currentTimeMillis();

        logger.info("Ts=================currency update [{}]", JsonUtils.object2String(notice));
    }

    @PacketReceiver
    public void atPlayerExpNotice(Session session, PlayerExpNotice response) {
        MiniClient miniClient = session.getChannel().attr(MiniClient.Attribute_Key).get();
        miniClient.lastTimePing = System.currentTimeMillis();

        logger.info("Ts=================player exp update [{}]", JsonUtils.object2String(response));
    }
}
