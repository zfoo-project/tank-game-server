package com.zfoo.tank.common.protocol.room;

import com.zfoo.protocol.anno.Note;
import com.zfoo.protocol.anno.Protocol;
import com.zfoo.tank.common.protocol.common.PlayerInfo;

import java.util.List;
public record ExitRoomNotice (
    Room room,
    @Note("匹配到的玩家基本数据")
    List<PlayerInfo> matchPlayerInfos
) {
}