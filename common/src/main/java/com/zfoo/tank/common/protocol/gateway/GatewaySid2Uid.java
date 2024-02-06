package com.zfoo.tank.common.protocol.gateway;


public class GatewaySid2Uid {

    private long sid;
    private long uid;

    public GatewaySid2Uid() {
    }

    public GatewaySid2Uid(long sid, long uid) {
        this.sid = sid;
        this.uid = uid;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
