package com.zfoo.tank.common.protocol.transfer;

public class RoomToHomeTransfer {

    private long roomId;
    private long uid;

    // ---------------------------------------------------------------------------------------------------------------
    private Room2HomeJoinTransfer joinRoomTransfer;

    public RoomToHomeTransfer() {
    }

    public RoomToHomeTransfer(long roomId, long uid) {
        this.roomId = roomId;
        this.uid = uid;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public Room2HomeJoinTransfer getJoinRoomTransfer() {
        return joinRoomTransfer;
    }

    public void setJoinRoomTransfer(Room2HomeJoinTransfer joinRoomTransfer) {
        this.joinRoomTransfer = joinRoomTransfer;
    }
}
