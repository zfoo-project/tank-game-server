
package com.zfoo.tank.common.protocol.room;

public class Admin2RoomStopDsTransfer {

    private long roomId;

    public Admin2RoomStopDsTransfer() {
    }

    public Admin2RoomStopDsTransfer(long roomId) {
        this.roomId = roomId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }
}
