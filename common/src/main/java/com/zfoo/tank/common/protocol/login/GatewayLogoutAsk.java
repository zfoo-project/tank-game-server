package com.zfoo.tank.common.protocol.login;

public class GatewayLogoutAsk {

    private long uid;

    public GatewayLogoutAsk() {
    }

    public GatewayLogoutAsk(long uid) {
        this.uid = uid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
