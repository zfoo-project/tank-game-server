package com.zfoo.tank.gateway.service;

import com.zfoo.net.NetContext;
import com.zfoo.net.session.Session;
import com.zfoo.tank.common.protocol.gateway.GatewaySid2Uid;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


@Component
public class GatewayService {

    public List<GatewaySid2Uid> afterLoginSid2Uids() {
        var list = new ArrayList<GatewaySid2Uid>();
        NetContext.getSessionManager().forEachServerSession(new Consumer<Session>() {
            @Override
            public void accept(Session session) {
                var sid = session.getSid();
                var uid = session.getUid();
                if (uid > 0) {
                    list.add(new GatewaySid2Uid(sid, uid));
                }
            }
        });
        return list;
    }

}
