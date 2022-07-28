package com.zfoo.tank.common.generate;

public enum ProtoModule {
    
    NATIVE(1, "native", 0, 100, new String[]{"com.zfoo.net.router.attachment", "com.zfoo.net.core.gateway.model"}),
    COMMON(2, "common", 100, 1000, new String[]{"com.zfoo.net.packet.common", "com.zfoo.tank.common.protocol.common"}),
    HOME(3, "tankHome", 1000, 10000, new String[]{"com.zfoo.tank.common.protocol.login", "com.zfoo.tank.common.protocol.battle", "com.zfoo.tank.common.protocol"}),
    CACHE(4, "tankCache", 3000, 4000, new String[]{"com.zfoo.tank.common.protocol.cache"}),
    ;
    public final int id;
    public final String name;
    public final int minId;
    public final int maxId;
    public final String[] modules;
    
    private ProtoModule(int id, String name, int minId, int maxId, String[] modules) {
        this.id = id;
        this.name = name;
        this.minId = minId;
        this.maxId = maxId;
        this.modules = modules;
    }
}
