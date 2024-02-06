package com.zfoo.tank.common.protocol.room;

import com.zfoo.protocol.anno.Protocol;
public record JoinRoomRequest (
    long roomId
) {
}