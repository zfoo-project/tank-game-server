package com.zfoo.tank.home.pressure;

import com.zfoo.net.NetContext;
import com.zfoo.net.core.AbstractClient;
import com.zfoo.net.core.HostAndPort;
import com.zfoo.net.core.websocket.WebsocketClient;
import com.zfoo.net.core.websocket.WebsocketSslClient;
import com.zfoo.net.session.Session;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolConfig;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicInteger;

public class MiniClient {
    public static final AtomicInteger createId = new AtomicInteger();
    public static final AttributeKey<MiniClient> Attribute_Key = AttributeKey.valueOf("11111111");

    private AbstractClient<SocketChannel> ChannelClient;
    private Session clientSession;


    public String account;
    public String pswd;
    public String token;
    public long lastTimePing;
    public long lastTimeMove;

    public MiniClient(String url) {
        String[] ss = url.split("[/\\\\]");
        var webSocketClientProtocolConfig = WebSocketClientProtocolConfig.newBuilder().webSocketUri("wss://local.zzmoon.com:18000").build();
        if (url.startsWith("wss")) {
            ChannelClient = new WebsocketSslClient(HostAndPort.valueOf(ss[2]), webSocketClientProtocolConfig);
        } else {
            ChannelClient = new WebsocketClient(HostAndPort.valueOf(ss[2]), webSocketClientProtocolConfig);
        }
        clientSession = ChannelClient.start();
        clientSession.getChannel().attr(Attribute_Key).set(this);

        lastTimePing = System.currentTimeMillis();
        lastTimeMove = System.currentTimeMillis();
        account = "xiao_0119_" + createId.addAndGet(1);
        pswd = account;
    }

    public void closeTry() {
        try {
            clientSession.close();
        } catch (Exception e) {

        }
    }

    public void sendMsg(Object packet) {
        NetContext.getRouter().send(clientSession, packet);
    }
}
