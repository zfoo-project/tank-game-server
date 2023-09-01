package com.zfoo.tank.common.generate;

public enum ProtoModule {
    
    NATIVE(1, "native", new String[]{"com.zfoo.net.router.attachment", "com.zfoo.net.core.gateway.model"}),
    COMMON(2, "common", new String[]{"com.zfoo.net.packet.common", "com.zfoo.tank.common.protocol.common"}),
    HOME(3, "tankHome", new String[]{"com.zfoo.tank.common.protocol.login", "com.zfoo.tank.common.protocol.battle", "com.zfoo.tank.common.protocol"}),
    CACHE(4, "tankCache", new String[]{"com.zfoo.tank.common.protocol.cache"}),
    ;
    public final int id;
    public final String name;
    public final String[] modules;
    
    private ProtoModule(int id, String name,String[] modules) {
        this.id = id;
        this.name = name;

        this.modules = modules;
    }
}
