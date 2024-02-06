package com.zfoo.tank.common.protocol.room;

import com.zfoo.protocol.anno.Note;
import com.zfoo.protocol.anno.Protocol;
@Note("匹配玩家的基本信息")
public record CreateRoomRequest (
    String title,
    String subTitle,
    String description,
    @Note("公开或者私密")
    boolean visible,
    String password,
    String icon,
    @Note("游戏类型")
    int gameId,
    @Note("游戏人数")
    int maxNums
) {
}