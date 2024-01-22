package com.zfoo.tank.common.protocol.login;

public class KickPlayerAsk {

    private long sid;
    private long uid;

    public KickPlayerAsk() {
    }

    public KickPlayerAsk(long sid, long uid) {
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
