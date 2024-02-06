package com.zfoo.tank.common.protocol.push;


import com.zfoo.tank.common.protocol.gateway.GatewaySid2Uid;

import java.util.List;

public class GatewaySessionUpdatePush {

    private List<GatewaySid2Uid> sid2Uids;

    public GatewaySessionUpdatePush() {
    }

    public GatewaySessionUpdatePush(List<GatewaySid2Uid> sid2Uids) {
        this.sid2Uids = sid2Uids;
    }

    public List<GatewaySid2Uid> getSid2Uids() {
        return sid2Uids;
    }

    public void setSid2Uids(List<GatewaySid2Uid> sid2Uids) {
        this.sid2Uids = sid2Uids;
    }
}
