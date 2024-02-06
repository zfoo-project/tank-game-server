package com.zfoo.tank.push.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zfoo.net.NetContext;
import com.zfoo.net.session.Session;
import com.zfoo.net.util.SessionUtils;
import com.zfoo.protocol.collection.ArrayListLong;
import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.scheduler.util.TimeUtils;
import com.zfoo.tank.common.protocol.gateway.GatewaySid2ConsumerSid;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Component
public class PushService {

    /**
     * 考虑到多端登录，一个网关session可能对应多个uid，一个uid可能对于多个sid
     */
    public Cache<Long, GatewaySid2ConsumerSid> gatewaySessionCaches = Caffeine.newBuilder()
            .expireAfterAccess(2 * TimeUtils.MILLIS_PER_HOUR, TimeUnit.MILLISECONDS)
            .maximumSize(10000).build();


    public void addGatewaySid(Session session, long gatewaySid, long uid) {
        gatewaySessionCaches.put(uid, new GatewaySid2ConsumerSid(gatewaySid, session.getSid()));
    }

    public void removeGatewaySid(Session session, long gatewaySid, long uid) {
        var gatewaySid2ConsumerSid = gatewaySessionCaches.getIfPresent(uid);
        if (gatewaySid2ConsumerSid == null) {
            return;
        }
        // session相等才移除
        if (gatewaySid2ConsumerSid.getConsumerSid() == session.getSid() && gatewaySid2ConsumerSid.getGatewaySid() == gatewaySid) {
            gatewaySessionCaches.invalidate(uid);
        }
    }

    @Nullable
    public GatewaySid2ConsumerSid getGatewaySession(long uid) {
        return gatewaySessionCaches.getIfPresent(uid);
    }

    public List<GatewaySid2ConsumerSid> getGatewaySessions(List<Long> uids) {
        if (CollectionUtils.isEmpty(uids)) {
            return Collections.emptyList();
        }
        var list = new ArrayList<GatewaySid2ConsumerSid>();
        for (var uid : uids) {
            var gatewaySid2ConsumerSid = getGatewaySession(uid);
            if (gatewaySid2ConsumerSid == null) {
                continue;
            }
            list.add(gatewaySid2ConsumerSid);
        }
        return list;
    }

    /**
     * uid转为换gateway可以识别的sid
     */
    public Map<Session, List<Long>> uidToGatewaySid(List<Long> uids) {
        var map = new HashMap<Session, List<Long>>();
        var gatewaySid2ConsumerSids = getGatewaySessions(uids);
        for (var gatewaySid2ConsumerSid : gatewaySid2ConsumerSids) {
            var gatewaySid = gatewaySid2ConsumerSid.getGatewaySid();
            var consumerSid = gatewaySid2ConsumerSid.getConsumerSid();
            var session = NetContext.getSessionManager().getServerSession(consumerSid);
            if (!SessionUtils.isActive(session)) {
                continue;
            }
            var gatewaySids = map.computeIfAbsent(session, it -> new ArrayListLong(uids.size()));
            gatewaySids.add(gatewaySid);
        }
        return map;
    }
}
