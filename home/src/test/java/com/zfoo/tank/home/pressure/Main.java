package com.zfoo.tank.home.pressure;

import com.zfoo.tank.common.constant.TankDeployEnum;
import com.zfoo.tank.common.protocol.cache.ScoreRankRequest;
import com.zfoo.tank.common.protocol.login.LoginRequest;
import com.zfoo.tank.home.controller.BattleController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaorenping
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(BattleController.class);

    public static Random Rand = new Random();
    public static ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(4);
    public static ConcurrentHashMap<Integer, MiniClient> allClient = new ConcurrentHashMap<>();

    static {
        TankDeployEnum.initDefaultEnv();
    }


    public static void main(String[] args) throws Exception {
        var context = new ClassPathXmlApplicationContext("my-tank-application.xml");

//        var myTankClient = new TcpClient(HostAndPort.valueOf(NetUtils.LOCAL_LOOPBACK_IP, Application.TCP_SERVER_PORT));
//        var myTankSession = myTankClient.start();
//        ThreadUtils.sleep(TimeUtils.MILLIS_PER_SECOND);

//        var webSocketClientProtocolConfig = WebSocketClientProtocolConfig.newBuilder()
//                .webSocketUri("http://127.0.0.1:18000/websocket").build();
//        var myTankClient = new WebsocketClient(HostAndPort.valueOf("127.0.0.1:18000"), webSocketClientProtocolConfig);
//        var myTankSession = myTankClient.start();
//
//        var webSocketClientProtocolConfig = WebSocketClientProtocolConfig.newBuilder()
//                .webSocketUri("wss://local.zzmoon.com:18000").build();
//        var myTankClient = new WebsocketSslClient(HostAndPort.valueOf("local.zzmoon.com:18000"), webSocketClientProtocolConfig);
//        var myTankSession = myTankClient.start();
//        ThreadUtils.sleep(TimeUtils.MILLIS_PER_SECOND);
        int PlayerNum = 10; //最大账号数
        int TimeInterval = 1; //每秒多少个账号登录
        // addTask
        AtomicInteger addTask = new AtomicInteger();
        // 发起的总连接数
        AtomicInteger totalConn = new AtomicInteger();
        // 检查存活===关闭非存活
        final int MAX_KEEP_TIME = 15 * 1000;
        executor.scheduleWithFixedDelay(() -> {
            int movePlayer = 0;
            int closeGs = 0;
            long curTime = System.currentTimeMillis();
            Iterator<MiniClient> it = allClient.values().iterator();
            while (it.hasNext()) {
                MiniClient client = it.next();
                boolean toClose = false;
                if (curTime - client.lastTimePing > MAX_KEEP_TIME) {
                    toClose = true;
                    closeGs++;
                }
                if (toClose) {
                    client.closeTry();
                    it.remove();
                    continue;
                }
                if (curTime - client.lastTimeMove < MAX_KEEP_TIME) {
                    movePlayer++;
                }
            }
            int executor_queue = executor.getQueue().size();
            logger.info("onlinelive" + ",总任务数addTask=" + addTask + ",总连接数total=" + totalConn.get() + ",存活live=" + allClient.size() + ",movePlayer=" + movePlayer + ",executor_queue=" + executor_queue + ",closeGs=" + closeGs);
        }, 3000, 3000, TimeUnit.MILLISECONDS);

        // 发送一个获取分数排行榜的信息
        final long Look_Rank_Time = 10000;
        executor.scheduleWithFixedDelay(() -> {
            for (MiniClient client : allClient.values()) {
                executor.schedule(() -> {
                    var scoreRankRequest = ScoreRankRequest.valueOf();
                    client.sendMsg(scoreRankRequest);
                }, Rand.nextInt((int) Look_Rank_Time), TimeUnit.MILLISECONDS);
            }
        }, Look_Rank_Time, Look_Rank_Time, TimeUnit.MILLISECONDS);

        // 开始执行连接--->登录请求
        while (true) {
            //if (totalConn.get() < PlayerNum) {
            if (allClient.size() < PlayerNum) {
                for (int i = 0; i < TimeInterval; i++) {
                    addTask.incrementAndGet();
                    executor.submit(() -> {
                        try {
                            MiniClient miniClient = new MiniClient("wss://local.zzmoon.com:18000"); //指定网关---本地网关127.0.0.1
                            //ws连接到通信有延迟
                            executor.schedule(() -> {
                                allClient.put(miniClient.hashCode(), miniClient);
                                totalConn.incrementAndGet();
                                // 登录请求
                                var loginRequest = LoginRequest.valueOf(miniClient.account, miniClient.pswd);
                                miniClient.sendMsg(loginRequest);
                            }, 500, TimeUnit.MILLISECONDS);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
            Thread.sleep(1000);
        }

//        // 模拟客户端，发送一个登录请求
//        var loginRequest = LoginRequest.valueOf("tank2", "tank2");
//        log.info("Ts=================send=" + loginRequest);
//        NetContext.getRouter().send(myTankSession, loginRequest);
//        ThreadUtils.sleep(2 * TimeUtils.MILLIS_PER_SECOND);
//
//        // 发送一个战斗结果请求
//        var battleResultRequest = BattleResultRequest.valueOf(RandomUtils.randomInt());
//        log.info("Ts=================send=" + battleResultRequest);
//        NetContext.getRouter().send(myTankSession, battleResultRequest);
//        ThreadUtils.sleep(TimeUtils.MILLIS_PER_SECOND);
//
//        // 发送一个获取分数排行榜的信息
//        var scoreRankRequest = ScoreRankRequest.valueOf();
//        log.info("Ts=================send=" + scoreRankRequest);
//        NetContext.getRouter().send(myTankSession, scoreRankRequest);
//        ThreadUtils.sleep(TimeUtils.MILLIS_PER_SECOND);
//
//        var scoreRankSyncResponse = NetContext.getRouter().syncAsk(myTankSession, scoreRankRequest, ScoreRankResponse.class, null).packet();
//        log.info("Ts=================sync 排行榜 [{}]", JsonUtils.object2String(scoreRankSyncResponse));
//        ThreadUtils.sleep(TimeUtils.MILLIS_PER_SECOND);
//
//        NetContext.getRouter().asyncAsk(myTankSession, scoreRankRequest, ScoreRankResponse.class, null)
//                .whenComplete(new Consumer<>() {
//                    @Override
//                    public void accept(ScoreRankResponse scoreRankResponse) {
//                        log.info("Ts=================async 排行榜 [{}]", JsonUtils.object2String(scoreRankResponse));
//                    }
//                });
//        ThreadUtils.sleep(TimeUtils.MILLIS_PER_SECOND);
    }

}
