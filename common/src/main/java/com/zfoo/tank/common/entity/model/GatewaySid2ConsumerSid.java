package com.zfoo.tank.common.entity.model;


public class GatewaySid2ConsumerSid {
    private long gatewaySid;
    private long consumerSid;

    public void update(long gatewaySid, long consumerSid) {
        this.gatewaySid = gatewaySid;
        this.consumerSid = consumerSid;
    }

    public long getGatewaySid() {
        return gatewaySid;
    }

    public void setGatewaySid(long gatewaySid) {
        this.gatewaySid = gatewaySid;
    }

    public long getConsumerSid() {
        return consumerSid;
    }

    public void setConsumerSid(long consumerSid) {
        this.consumerSid = consumerSid;
    }
}
