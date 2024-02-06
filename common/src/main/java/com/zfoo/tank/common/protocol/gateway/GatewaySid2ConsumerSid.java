package com.zfoo.tank.common.protocol.gateway;


public class GatewaySid2ConsumerSid {
    private long gatewaySid;
    private long consumerSid;

    public void update(long gatewaySid, long consumerSid) {
        this.gatewaySid = gatewaySid;
        this.consumerSid = consumerSid;
    }

    public void clear() {
        update(0, 0);
    }


    public GatewaySid2ConsumerSid() {
    }

    public GatewaySid2ConsumerSid(long gatewaySid, long consumerSid) {
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
