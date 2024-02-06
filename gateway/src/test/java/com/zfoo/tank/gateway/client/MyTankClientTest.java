/*
 * Copyright (C) 2020 The zfoo Authors
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.tank.gateway.client;

import com.zfoo.net.NetContext;
import com.zfoo.net.core.HostAndPort;
import com.zfoo.net.core.tcp.TcpClient;
import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.protocol.util.RandomUtils;
import com.zfoo.protocol.util.ThreadUtils;
import com.zfoo.scheduler.util.TimeUtils;
import com.zfoo.tank.common.constant.TankDeployEnum;
import com.zfoo.tank.common.protocol.battle.BattleResultRequest;
import com.zfoo.tank.common.protocol.cache.ScoreRankRequest;
import com.zfoo.tank.common.protocol.cache.ScoreRankResponse;
import com.zfoo.tank.common.protocol.login.GetPlayerInfoRequest;
import com.zfoo.tank.common.protocol.login.GetPlayerInfoResponse;
import com.zfoo.tank.common.protocol.login.LoginByHttpTokenRequest;

import static com.zfoo.tank.common.util.HttpLoginUtils.*;

import com.zfoo.tank.common.protocol.room.CreateRoomRequest;
import com.zfoo.tank.common.protocol.room.CreateRoomResponse;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

/**
 * @author jaysunxiao
 */
@Ignore
public class MyTankClientTest {

    private static final Logger log = LoggerFactory.getLogger(MyTankClientTest.class);

    static {
        TankDeployEnum.initDefaultEnv();
    }

    @Test
    public void tankClient() throws Exception {
        var context = new ClassPathXmlApplicationContext("my-tank-application.xml");

        // http登录
        var loginResult = loginByHttp("玩家3");
        log.info("http loginResult:[{}]", JsonUtils.object2String(loginResult));

        var connectUrl = RandomUtils.randomEle(loginResult.getConnectUrls());
        var myTankClient = new TcpClient(HostAndPort.valueOf(connectUrl));
        var session = myTankClient.start();

        // 模拟客户端，发送一个登录请求
        var loginRequest = LoginByHttpTokenRequest.valueOf(loginResult.getMyToken());
        NetContext.getRouter().send(session, loginRequest);
        ThreadUtils.sleep(2 * TimeUtils.MILLIS_PER_SECOND);

        // 登录过后获取用户信息
        var getPlayerInfoResponse = NetContext.getRouter().syncAsk(session, new GetPlayerInfoRequest(), GetPlayerInfoResponse.class, null).packet();
        log.info("sync 获取用户信息 [{}]", JsonUtils.object2String(getPlayerInfoResponse));

        // 发送一个战斗结果请求
        var battleResultRequest = BattleResultRequest.valueOf(RandomUtils.randomInt());
        NetContext.getRouter().send(session, battleResultRequest);
        ThreadUtils.sleep(TimeUtils.MILLIS_PER_SECOND);

        // 发送一个获取分数排行榜的信息
        var scoreRankRequest = ScoreRankRequest.valueOf();
        NetContext.getRouter().send(session, scoreRankRequest);
        ThreadUtils.sleep(TimeUtils.MILLIS_PER_SECOND);

        var scoreRankSyncResponse = NetContext.getRouter().syncAsk(session, scoreRankRequest, ScoreRankResponse.class, null).packet();
        log.info("sync 排行榜 [{}]", JsonUtils.object2String(scoreRankSyncResponse));
        ThreadUtils.sleep(TimeUtils.MILLIS_PER_SECOND);

        NetContext.getRouter().asyncAsk(session, scoreRankRequest, ScoreRankResponse.class, null)
                .whenComplete(new Consumer<ScoreRankResponse>() {
                    @Override
                    public void accept(ScoreRankResponse scoreRankResponse) {
                        log.info("async 排行榜 [{}]", JsonUtils.object2String(scoreRankResponse));
                    }
                });
        ThreadUtils.sleep(TimeUtils.MILLIS_PER_SECOND);

        var createRoomRequest = new CreateRoomRequest("我的房间", "子标题", "我的描述", true, "", "", 0, 6);
        var createResponse = NetContext.getRouter().syncAsk(session, createRoomRequest, CreateRoomResponse.class, null).packet();
        log.info("create room [{}]", JsonUtils.object2String(createResponse));

        ThreadUtils.sleep(Long.MAX_VALUE);
    }


    // ---------------------------------------------------------------------------------------------------------

    public HttpLoginResult loginByHttp(String name) throws IOException, InterruptedException {
        var client = HttpClient.newBuilder().build();

        var responseBodyHandler = HttpResponse.BodyHandlers.ofString();

        var loginRequest = new HttpLoginRequest();
        loginRequest.setToken(name);

        var request = HttpRequest
                .newBuilder(URI.create("http://localhost:13003/api/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JsonUtils.object2String(loginRequest)))
                .build();

        var json = client.send(request, responseBodyHandler).body();
        log.info("http login [{}]", json);

        var response = JsonUtils.string2Object(json, HttpLoginResponse.class);
        return response.getData();
    }

    public static class HttpLoginResponse {
        private int code;
        private String message;
        private HttpLoginResult data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public HttpLoginResult getData() {
            return data;
        }

        public void setData(HttpLoginResult data) {
            this.data = data;
        }
    }
}
